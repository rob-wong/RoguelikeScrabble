package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyCreationMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import javax.inject.Inject

class AdvanceToNextEnemyUseCase @Inject constructor(
	private val enemyCreationMapper: EnemyCreationMapper,
) {
	companion object {
		private const val LEVELS_PER_STAGE = 4
	}

	operator fun invoke(game: ActiveGameState): ActiveGameState {
		val (newStage, newLevel) = calculateNextStageAndLevel(
			currentStage = game.activeGameVariables.stage,
			currentLevel = game.activeGameVariables.level
		)
		val newEnemyHealth = calculateNewEnemyHealth(newStage, newLevel)
		return createUpdatedGameState(game, newStage, newLevel, newEnemyHealth)
	}

	private fun calculateNextStageAndLevel(currentStage: Int, currentLevel: Int): Pair<Int, Int> {
		return if (currentLevel >= LEVELS_PER_STAGE) {
			currentStage + 1 to 1
		} else {
			currentStage to currentLevel + 1
		}
	}

	private fun calculateNewEnemyHealth(stage: Int, level: Int): Int {
		return enemyCreationMapper.map(
			EnemyCreationMapper.Param(
				stage = stage,
				level = level
			)
		)
	}

	private fun createUpdatedGameState(
		game: ActiveGameState,
		newStage: Int,
		newLevel: Int,
		newEnemyHealth: Int
	): ActiveGameState {
		return game.copy(
			activeGameVariables = game.activeGameVariables.copy(
				stage = newStage,
				level = newLevel,
			),
			currentRound = game.currentRound.copy(
				round = 1,
				discardsUsed = 0,
				enemyHealth = newEnemyHealth,
				wordsPlayed = emptyList(),
			)
		)
	}
}

