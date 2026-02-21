package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.mappers.EnemyCreationMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.copy
import javax.inject.Inject

class AdvanceToNextEnemyUseCase @Inject constructor(
	private val enemyCreationMapper: EnemyCreationMapper,
	private val drawHandUseCase: DrawHandUseCase,
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
		val gameWithResetDeck = game.copy(
			activeGameVariables = game.activeGameVariables.copy(
				stage = newStage,
				level = newLevel,
				needsEffectSelection = false,
			),
			currentRound = game.currentRound.copy(
				round = 1,
				discardsUsed = 0,
				enemyHealth = newEnemyHealth,
				wordsPlayed = emptyList(),
				mutableDeck = game.activeGameValues.deck.copy(),
				hand = emptyList(),
				effects = emptyList(),
			)
		)
		
		return drawHandUseCase(
			drawnAmount = game.activeGameVariables.handSize,
			game = gameWithResetDeck
		)
	}
}

