package com.cypherose.business.gameplayLoop.data

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.interfaces.DataSource
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.AppDataModel
import com.cypherose.business.models.GameState
import com.cypherose.business.models.NoneGameState
import com.cypherose.business.models.copy
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
			dataSource.quitGame(game)
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
		val result = appDataModel.saveUser(
			appDataModel.getCurrentUser().copy(gameState = gameState)
		)
		return result.getOrNull()?.gameState ?: gameState
	}

	suspend fun endGame(gameState: ActiveGameState): Result<Unit> {
		val user = appDataModel.getCurrentUser()
		val earnedRunes = gameState.activeGameVariables.runesCount - gameState.activeGameVariables.startingRunesCount
		return appDataModel.saveUser(
			user.copy(
				runesCount = user.runesCount + earnedRunes,
				gameState = NoneGameState
			)
		).fold(
			onSuccess = { Result.success(Unit) },
			onFailure = { Result.failure(it) }
		)
	}

	suspend fun quitGame(gameState: ActiveGameState): Result<Unit> {
		val user = appDataModel.getCurrentUser()
		val earnedRunes = gameState.activeGameVariables.runesCount - gameState.activeGameVariables.startingRunesCount
		return appDataModel.saveUser(
			user.copy(
				runesCount = user.runesCount + earnedRunes,
				gameState = NoneGameState
			)
		).fold(
			onSuccess = { Result.success(Unit) },
			onFailure = { Result.failure(it) }
		)
	}
}
