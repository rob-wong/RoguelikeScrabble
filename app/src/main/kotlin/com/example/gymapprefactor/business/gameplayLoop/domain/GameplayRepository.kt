package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.GameState

interface GameplayRepository : Repository {
	suspend fun getGameState(): GameState
	suspend fun saveGameState(game: GameState): GameState
	suspend fun endGame(game: ActiveGameState, saveProgression: Boolean)
}
