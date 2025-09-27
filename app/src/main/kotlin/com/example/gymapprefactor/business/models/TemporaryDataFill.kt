package com.example.gymapprefactor.business.models

import javax.inject.Singleton

@Singleton
object TemporaryDataFill {
    val user = DefaultUser(
	    username = "Username",
	    runesCount = 100,
	    decks = listOf(),
	    unlockedEffects = listOf(),
	    gameState = NoneGameState()
    )
}
