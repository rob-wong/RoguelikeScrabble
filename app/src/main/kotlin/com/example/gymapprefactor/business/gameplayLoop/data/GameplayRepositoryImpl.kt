package com.example.gymapprefactor.business.gameplayLoop.data

import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.GameState
import com.example.gymapprefactor.business.models.NoneGameState
import com.example.gymapprefactor.business.models.copy
import javax.inject.Inject

class GameplayRepositoryImpl(
	override val dataSource: GameplayDataSource
) : GameplayRepository {
	override suspend fun getGameState(): GameState {
		return dataSource.fetchGameState()
	}

	override suspend fun saveGameState(game: GameState): GameState {
		return dataSource.saveGameState(game)
	}

	override suspend fun endGame(game: ActiveGameState, saveProgression: Boolean) {
		if (saveProgression) {
			dataSource.endGame(game)
		} else {
			dataSource.quitGame()
		}
	}
}

class GameplayDataSource @Inject constructor(
	private val appDataModel: AppDataModel,
) : DataSource {
	fun fetchGameState(): GameState {
		return appDataModel.getCurrentUser().gameState
	}

	suspend fun saveGameState(gameState: GameState): GameState {
		return appDataModel.saveUser(
			appDataModel.getCurrentUser().copy(gameState = gameState)
		).gameState
	}

	suspend fun endGame(gameState: ActiveGameState) {
		val user = appDataModel.getCurrentUser()
		appDataModel.saveUser(
			user.copy(
				runesCount = user.runesCount + gameState.activeGameVariables.runesCount,
				gameState = NoneGameState
			)
		)
	}

	suspend fun quitGame() {
		appDataModel.saveUser(
			appDataModel.getCurrentUser().copy(gameState = NoneGameState)
		)
	}
}
