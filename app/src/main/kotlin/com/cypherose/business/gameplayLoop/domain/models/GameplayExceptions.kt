package com.cypherose.business.gameplayLoop.domain.models

sealed class GameplayExceptions(message: String? = null) : Throwable(message) {
	data class InvalidWord(
		val word: String
	) : GameplayExceptions("Invalid Word: $word")
}
