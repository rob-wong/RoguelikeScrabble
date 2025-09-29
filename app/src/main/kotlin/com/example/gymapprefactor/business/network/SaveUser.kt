package com.example.gymapprefactor.business.network

import android.content.Context
import com.example.gymapprefactor.business.models.User
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Singleton

// If there is a backend developed, this is where the network calls will be
private const val USER_FILE = "user.json"

@Singleton
object UserStorage {
	private val gson = Gson()
	private val mutex = Mutex()
	private const val FILE_NAME = "user.json"

	suspend fun saveUser(context: Context, user: User) {
		mutex.withLock {
			withContext(Dispatchers.IO) {
				val file = File(context.filesDir, FILE_NAME)
				file.writeText(gson.toJson(user))
			}
		}
	}

	suspend fun loadUser(context: Context): User? {
		return mutex.withLock {
			withContext(Dispatchers.IO) {
				val file = File(context.filesDir, FILE_NAME)
				if (!file.exists()) return@withContext null
				gson.fromJson(file.readText(), User::class.java)
			}
		}
	}
}
