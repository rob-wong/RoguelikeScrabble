package com.example.gymapprefactor.business.network

import android.content.Context
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.errors.StorageException
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.DefaultDeck
import com.example.gymapprefactor.business.models.DefaultEffect
import com.example.gymapprefactor.business.models.DefaultLetter
import com.example.gymapprefactor.business.models.DefaultUser
import com.example.gymapprefactor.business.models.NoneGameState
import com.example.gymapprefactor.business.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_FILE = "user.json"
private const val USER_FILE_TEMP = "user.json.tmp"
private const val MAX_RETRY_ATTEMPTS = 3
private const val RETRY_DELAY_MS = 100L

@Singleton
class UserStorage @Inject constructor(
	private val dispatcherProvider: DispatcherProvider,
	@ApplicationContext private val context: Context,
) {
	private val json = Json {
		serializersModule = SerializersModule {
			polymorphic(User::class) {
				subclass(DefaultUser::class)
				subclass(com.example.gymapprefactor.business.models.NoneUser::class)
			}
			polymorphic(com.example.gymapprefactor.business.models.GameState::class) {
				subclass(ActiveGameState::class)
				subclass(NoneGameState::class)
			}
			polymorphic(com.example.gymapprefactor.business.models.Deck::class) {
				subclass(DefaultDeck::class)
			}
			polymorphic(com.example.gymapprefactor.business.models.Letter::class) {
				subclass(DefaultLetter::class)
			}
			polymorphic(com.example.gymapprefactor.business.models.Effect::class) {
				subclass(DefaultEffect::class)
			}
		}
		classDiscriminator = "type"
		ignoreUnknownKeys = true
		encodeDefaults = true
		useArrayPolymorphism = false
	}
	
	private val mutex = Mutex()

	suspend fun saveUser(user: User): Result<User> {
		return mutex.withLock {
			withContext(dispatcherProvider.io) {
				performSaveWithRetry(user)
			}
		}
	}

	suspend fun loadUser(): Result<User?> {
		return mutex.withLock {
			withContext(dispatcherProvider.io) {
				performLoadWithRetry()
			}
		}
	}

	private suspend fun performSaveWithRetry(user: User): Result<User> {
		val retryResult = performRetryLoop("save user") { attempt ->
			trySaveUser(user, attempt)
		}
		return retryResult ?: run {
			val error = StorageException.IOException("save user", IOException("Max retries exceeded"))
			Timber.e(error, "User save failed after $MAX_RETRY_ATTEMPTS attempts")
			Result.failure(error)
		}
	}

	private suspend fun <T> performRetryLoop(
		operation: String,
		attemptAction: suspend (Int) -> Result<T>
	): Result<T>? {
		for (attempt in 0 until MAX_RETRY_ATTEMPTS) {
			val attemptResult = attemptAction(attempt)
			val exception = attemptResult.exceptionOrNull()
			
			if (attemptResult.isSuccess || exception !is IOException) {
				return attemptResult
			}
			
			Timber.w(exception, "User $operation attempt ${attempt + 1} failed")
			if (attempt < MAX_RETRY_ATTEMPTS - 1) {
				delay(RETRY_DELAY_MS * (attempt + 1))
			}
		}
		
		return null
	}

	private fun trySaveUser(user: User, attempt: Int): Result<User> {
		return try {
			val result = saveUserAtomically(user)
			if (attempt > 0) {
				Timber.d("User save succeeded after $attempt retries")
			}
			Result.success(result)
		} catch (e: SecurityException) {
			val error = StorageException.PermissionException("save user", e)
			Timber.e(error, "Permission denied while saving user")
			Result.failure(error)
		} catch (e: SerializationException) {
			val error = StorageException.SerializationException("save user", e)
			Timber.e(error, "Serialization failed while saving user")
			Result.failure(error)
		} catch (e: Exception) {
			val error = StorageException.SerializationException("save user", e)
			Timber.e(error, "Unexpected error while saving user")
			Result.failure(error)
		}
	}

	private suspend fun performLoadWithRetry(): Result<User?> {
		val retryResult = performRetryLoop("load user") { attempt ->
			tryLoadUser(attempt)
		}
		return retryResult ?: run {
			val error = StorageException.IOException("load user", IOException("Max retries exceeded"))
			Timber.e(error, "User load failed after $MAX_RETRY_ATTEMPTS attempts")
			Result.failure(error)
		}
	}

	private fun tryLoadUser(attempt: Int): Result<User?> {
		return try {
			val result = loadUserSafely()
			if (attempt > 0) {
				Timber.d("User load succeeded after $attempt retries")
			}
			Result.success(result)
		} catch (e: SecurityException) {
			val error = StorageException.PermissionException("load user", e)
			Timber.e(error, "Permission denied while loading user")
			Result.failure(error)
		} catch (e: SerializationException) {
			val error = StorageException.CorruptedDataException(getUserFilePath())
			Timber.e(error, "User data file is corrupted")
			Result.failure(error)
		} catch (e: Exception) {
			val error = StorageException.SerializationException("load user", e)
			Timber.e(error, "Unexpected error while loading user")
			Result.failure(error)
		}
	}

	private fun saveUserAtomically(user: User): User {
		val file = File(context.filesDir, USER_FILE)
		val tempFile = File(context.filesDir, USER_FILE_TEMP)
		
		val jsonString = json.encodeToString(serializer<User>(), user)
		tempFile.writeText(jsonString)
		
		if (!tempFile.renameTo(file)) {
			tempFile.delete()
			throw IOException("Failed to rename temp file to user file")
		}
		
		return user
	}

	private fun loadUserSafely(): User? {
		val file = File(context.filesDir, USER_FILE)
		
		return when {
			!file.exists() -> null
			!file.canRead() -> throw IOException("Cannot read user file: ${file.absolutePath}")
			else -> {
				val fileContent = file.readText()
				if (fileContent.isBlank()) {
					Timber.w("User file is empty, treating as no user")
					null
				} else {
					json.decodeFromString(serializer<User>(), fileContent)
				}
			}
		}
	}

	private fun getUserFilePath(): String {
		return File(context.filesDir, USER_FILE).absolutePath
	}
}
