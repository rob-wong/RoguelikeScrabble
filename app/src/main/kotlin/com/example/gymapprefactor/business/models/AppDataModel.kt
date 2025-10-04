package com.example.gymapprefactor.business.models

import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.network.UserStorage
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class AppDataModel @Inject constructor(
    private val userStorage: UserStorage,
    private val dispatcherProvider: DispatcherProvider,
) {
    lateinit var user: User

    suspend fun fetchOrCreateUser() {
        withContext(dispatcherProvider.io) {
            Log.d("UserCreationTest", "Starting fetchOrCreateUser on thread: ${Thread.currentThread().name}")
            val fetchedUser = fetchUser()

            if (fetchedUser != null) {
                Log.d("UserCreationTest", "User found in storage: ${fetchedUser.username}")
                user = fetchedUser
            } else {
                Log.d("UserCreationTest", "User NOT found in storage. Calling createDefaultUser().")
                user = createDefaultUser()
            }
        }
    }

    private suspend fun fetchUser(): User? {
        Log.d("UserCreationTest", "Calling userStorage.loadUser()")
        return userStorage.loadUser()
    }

    private suspend fun createDefaultUser(): User {
        Log.d("UserCreationTest", "Inside createDefaultUser(). Creating 'Username'...")
        val newUser = DefaultUser(
            username = "Username",
            runesCount = 100,
            decks = listOf(createDefaultDeck()),
            unlockedEffects = listOf(),
            gameState = NoneGameState()
        )
        Log.d("UserCreationTest", "User object created. Calling userStorage.saveUser().")
        userStorage.saveUser(newUser)
        Log.d("UserCreationTest", "Finished userStorage.saveUser(). Returning new user.")
        return newUser
    }

    private fun createDefaultDeck(): Deck {
        return DefaultDeck(
	        id = "default-deck",
	        letters = createDefaultLetters(),
	        name = "Default Deck"
        )
    }

    private fun createDefaultLetters(): List<Letter> {
        return listOf(
            DefaultLetter(id = "a-1", letter = 'a', level = 1),
            DefaultLetter(id = "a-2", letter = 'a', level = 1),
            DefaultLetter(id = "b-1", letter = 'b', level = 1),
            DefaultLetter(id = "b-2", letter = 'b', level = 1),
            DefaultLetter(id = "c-1", letter = 'c', level = 1),
            DefaultLetter(id = "c-2", letter = 'c', level = 1),
            DefaultLetter(id = "d-1", letter = 'd', level = 1),
            DefaultLetter(id = "d-2", letter = 'd', level = 1),
            DefaultLetter(id = "e-1", letter = 'e', level = 1),
            DefaultLetter(id = "e-2", letter = 'e', level = 1),
            DefaultLetter(id = "f-1", letter = 'f', level = 1),
            DefaultLetter(id = "f-2", letter = 'f', level = 1),
            DefaultLetter(id = "g-1", letter = 'g', level = 1),
            DefaultLetter(id = "g-2", letter = 'g', level = 1),
            DefaultLetter(id = "h-1", letter = 'h', level = 1),
            DefaultLetter(id = "h-2", letter = 'h', level = 1),
            DefaultLetter(id = "i-1", letter = 'i', level = 1),
            DefaultLetter(id = "i-2", letter = 'i', level = 1),
            DefaultLetter(id = "j-1", letter = 'j', level = 1),
            DefaultLetter(id = "j-2", letter = 'j', level = 1),
            DefaultLetter(id = "k-1", letter = 'k', level = 1),
            DefaultLetter(id = "k-2", letter = 'k', level = 1),
            DefaultLetter(id = "l-1", letter = 'l', level = 1),
            DefaultLetter(id = "l-2", letter = 'l', level = 1),
            DefaultLetter(id = "m-1", letter = 'm', level = 1),
            DefaultLetter(id = "m-2", letter = 'm', level = 1),
            DefaultLetter(id = "n-1", letter = 'n', level = 1),
            DefaultLetter(id = "n-2", letter = 'n', level = 1),
            DefaultLetter(id = "o-1", letter = 'o', level = 1),
            DefaultLetter(id = "o-2", letter = 'o', level = 1),
            DefaultLetter(id = "p-1", letter = 'p', level = 1),
            DefaultLetter(id = "p-2", letter = 'p', level = 1),
            DefaultLetter(id = "q-1", letter = 'q', level = 1),
            DefaultLetter(id = "q-2", letter = 'q', level = 1),
            DefaultLetter(id = "r-1", letter = 'r', level = 1),
            DefaultLetter(id = "r-2", letter = 'r', level = 1),
            DefaultLetter(id = "s-1", letter = 's', level = 1),
            DefaultLetter(id = "s-2", letter = 's', level = 1),
            DefaultLetter(id = "t-1", letter = 't', level = 1),
            DefaultLetter(id = "t-2", letter = 't', level = 1),
            DefaultLetter(id = "u-1", letter = 'u', level = 1),
            DefaultLetter(id = "u-2", letter = 'u', level = 1),
            DefaultLetter(id = "v-1", letter = 'v', level = 1),
            DefaultLetter(id = "v-2", letter = 'v', level = 1),
            DefaultLetter(id = "w-1", letter = 'w', level = 1),
            DefaultLetter(id = "w-2", letter = 'w', level = 1),
            DefaultLetter(id = "x-1", letter = 'x', level = 1),
            DefaultLetter(id = "x-2", letter = 'x', level = 1),
            DefaultLetter(id = "y-1", letter = 'y', level = 1),
            DefaultLetter(id = "y-2", letter = 'y', level = 1),
            DefaultLetter(id = "z-1", letter = 'z', level = 1),
            DefaultLetter(id = "z-2", letter = 'z', level = 1)
        )
    }
}
