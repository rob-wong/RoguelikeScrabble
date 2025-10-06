package com.example.gymapprefactor.business.gameplayLoop.data

import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.GameState
import javax.inject.Inject

class GameplayRepositoryImpl(
	override val dataSource: GameplayDataSource
) : GameplayRepository {
	override suspend fun getGameState(): GameState {
		return dataSource.fetchGameState()
	}
}

class GameplayDataSource @Inject constructor(
	private val appDataModel: AppDataModel,
) : DataSource {
	fun fetchGameState(): GameState {
		return appDataModel.user.gameState
	}
}
