package com.example.gymapprefactor.business.network

import android.content.Context
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.DefaultDeck
import com.example.gymapprefactor.business.models.DefaultEffect
import com.example.gymapprefactor.business.models.DefaultLetter
import com.example.gymapprefactor.business.models.DefaultUser
import com.example.gymapprefactor.business.models.NoneGameState
import com.example.gymapprefactor.business.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_FILE = "user.json"

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

	suspend fun saveUser(user: User): User {
		mutex.withLock {
			withContext(dispatcherProvider.io) {
				val file = File(context.filesDir, USER_FILE)
				file.writeText(json.encodeToString(serializer<User>(), user))
			}
		}
		return user
	}

	suspend fun loadUser(): User? {
		return mutex.withLock {
			withContext(dispatcherProvider.io) {
				val file = File(context.filesDir, USER_FILE)
				if (!file.exists()) return@withContext null
				json.decodeFromString(serializer<User>(), file.readText())
			}
		}
	}
}
