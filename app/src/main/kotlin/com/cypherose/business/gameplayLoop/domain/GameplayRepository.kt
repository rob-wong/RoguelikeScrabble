package com.cypherose.business.gameplayLoop.domain

import com.cypherose.business.interfaces.Repository
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.GameState

interface GameplayRepository : Repository {
	suspend fun getGameState(): GameState
	suspend fun saveGameState(game: GameState): GameState
	suspend fun endGame(game: ActiveGameState, saveProgression: Boolean)
	fun getPreviouslyPlayedEffects(): List<String>
	suspend fun addPreviouslyPlayedEffect(label: String)
}
