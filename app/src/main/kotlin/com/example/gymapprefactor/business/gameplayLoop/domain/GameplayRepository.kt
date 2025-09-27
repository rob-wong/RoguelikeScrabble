package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.GameState

interface GameplayRepository : Repository {
	suspend fun getGameState(): GameState
}
