package com.example.gymapprefactor.business.gameplayLoop.data

import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.GameState

class GameplayRepositoryImpl(
	override val dataSource: GameplayDataSource
) : GameplayRepository {
	override suspend fun getGameState(): GameState {
		return dataSource.fetchGameState()
	}
}

class GameplayDataSource : DataSource {
	fun fetchGameState(): GameState {
		return AppDataModel.user.gameState
	}
}
