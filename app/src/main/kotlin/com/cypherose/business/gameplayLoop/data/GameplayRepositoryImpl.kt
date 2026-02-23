package com.cypherose.business.gameplayLoop.data

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.interfaces.DataSource
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.AppDataModel
import com.cypherose.business.models.DefaultUser
import com.cypherose.business.models.GameState
import com.cypherose.business.models.NoneGameState
import com.cypherose.business.models.copy
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

	override fun getPreviouslyPlayedEffects(): List<String> {
		return dataSource.getPreviouslyPlayedEffects()
	}

	override suspend fun addPreviouslyPlayedEffect(label: String) {
		dataSource.addPreviouslyPlayedEffect(label)
	}
}

class GameplayDataSource @Inject constructor(
	private val appDataModel: AppDataModel,
) : DataSource {
	private val addEffectMutex = Mutex()

	fun fetchGameState(): GameState {
		val user = appDataModel.getCurrentUser()
		return mergePreviouslyPlayedEffects(user.gameState, user.previouslyPlayedEffects)
	}

	fun getPreviouslyPlayedEffects(): List<String> {
		return appDataModel.getCurrentUser().previouslyPlayedEffects
	}

	private fun mergePreviouslyPlayedEffects(gameState: GameState, labels: List<String>): GameState {
		return if (gameState is ActiveGameState) {
			gameState.copy(previouslyPlayedEffectLabels = labels)
		} else {
			gameState
		}
	}

	suspend fun addPreviouslyPlayedEffect(label: String) {
		addEffectMutex.withLock {
			val user = appDataModel.getCurrentUser()
			if (user !is DefaultUser || label in user.previouslyPlayedEffects) return
			val updatedUser = user.copy(previouslyPlayedEffects = user.previouslyPlayedEffects + label)
			appDataModel.saveUser(updatedUser).getOrThrow()
		}
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
