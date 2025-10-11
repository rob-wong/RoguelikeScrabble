package com.example.gymapprefactor.business.network

import android.content.Context
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.DefaultDeck
import com.example.gymapprefactor.business.models.DefaultLetter
import com.example.gymapprefactor.business.models.DefaultUser
import com.example.gymapprefactor.business.models.GameState
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.models.NoneGameState
import com.example.gymapprefactor.business.models.User
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_FILE = "user.json"

// this is a more java-based approach, it could be easily changed to using kotlin's
// serialization, I'm just more used to dealing with gson
@Singleton
class UserStorage @Inject constructor(
	private val dispatcherProvider: DispatcherProvider,
	@ApplicationContext private val context: Context,
){
	private val deckAdapterFactory = RuntimeTypeAdapterFactory
		.of(Deck::class.java, "type")
		.registerSubtype(DefaultDeck::class.java, "default")

	private val letterAdapterFactory = RuntimeTypeAdapterFactory
		.of(Letter::class.java, "type")
		.registerSubtype(DefaultLetter::class.java, "default")
	private val gameStateAdapterFactory = RuntimeTypeAdapterFactory
		.of(GameState::class.java, "type")
		.registerSubtype(ActiveGameState::class.java, "active")
		.registerSubtype(NoneGameState::class.java, "none")
	private val userAdapterFactory = RuntimeTypeAdapterFactory
		.of(User::class.java, "type")
		.registerSubtype(DefaultUser::class.java, "default")

	private val gson = GsonBuilder()
		.registerTypeAdapterFactory(userAdapterFactory)
		.registerTypeAdapterFactory(gameStateAdapterFactory)
		.registerTypeAdapterFactory(deckAdapterFactory)
		.registerTypeAdapterFactory(letterAdapterFactory)
		.create()
	private val mutex = Mutex()

	suspend fun saveUser(user: User): User {
		mutex.withLock {
			withContext(dispatcherProvider.io) {
				val file = File(context.filesDir, USER_FILE)
				file.writeText(gson.toJson(user, User::class.java))
			}
		}
		return user
	}

	suspend fun loadUser(): User? {
		return mutex.withLock {
			withContext(dispatcherProvider.io) {
				val file = File(context.filesDir, USER_FILE)
				if (!file.exists()) return@withContext null
				gson.fromJson(file.readText(), User::class.java)
			}
		}
	}
}
