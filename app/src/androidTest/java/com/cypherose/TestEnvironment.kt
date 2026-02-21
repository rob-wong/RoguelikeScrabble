package com.cypherose.testutil

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.cypherose.business.models.DefaultUser
import com.cypherose.business.models.NoneGameState
import com.cypherose.business.models.User
import java.io.File
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

object TestEnvironment {

	private const val USER_FILE = "user.json"

	private val context: Context
		get() = InstrumentationRegistry.getInstrumentation().targetContext

	fun setUserPresentEnvironment(user: User = defaultUser()) {
		val file = File(context.filesDir, USER_FILE)
		file.writeText(
			"""
            {
              "type": "default",
              "username": "${user.username}",
              "runesCount": ${user.runesCount},
              "decks": [],
              "unlockedEffects": [],
              "gameState": { "type": "none" }
            }
            """.trimIndent()
		)
	}

	fun setNoUserEnvironment() {
		val file = File(context.filesDir, USER_FILE)
		if (file.exists()) file.delete()
	}

	suspend fun waitUntil(
		timeout: Duration = 5.seconds,
		pollInterval: Duration = 100.milliseconds,
		condition: suspend () -> Boolean,
	) {
		val timeSource = TimeSource.Monotonic
		val deadline = timeSource.markNow() + timeout

		while (deadline.hasNotPassedNow()) {
			if (condition()) { return }
			kotlinx.coroutines.delay(pollInterval)
		}

		throw Throwable("Condition was not met within ${timeout.inWholeSeconds} seconds.")
	}

	private fun defaultUser(): DefaultUser = DefaultUser(
		username = "PreSavedUser",
		runesCount = 777,
		decks = emptyList(),
		unlockedEffects = emptyList(),
		gameState = NoneGameState
	)
}
