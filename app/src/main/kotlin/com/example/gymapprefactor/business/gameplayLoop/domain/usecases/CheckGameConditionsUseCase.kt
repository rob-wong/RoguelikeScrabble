package com.example.gymapprefactor.business.gameplayLoop.domain.usecases

import com.example.gymapprefactor.business.gameplayLoop.domain.GameRules
import com.example.gymapprefactor.business.models.ActiveGameState
import javax.inject.Inject

data class GameConditionResult(
	val isWon: Boolean,
	val isLost: Boolean,
	val updatedGame: ActiveGameState
)

class CheckGameConditionsUseCase @Inject constructor(
	private val gameRules: GameRules,
	private val advanceToNextEnemyUseCase: AdvanceToNextEnemyUseCase,
) {
	operator fun invoke(game: ActiveGameState): GameConditionResult {
		val isWon = gameRules.checkWinCondition(game)
		val isLost = gameRules.checkLossCondition(game) && !isWon

		val updatedGame = if (isWon) {
			advanceToNextEnemyUseCase(game)
		} else {
			game.copy(
				activeGameVariables = game.activeGameVariables.copy(
					gameLost = isLost
				)
			)
		}

		return GameConditionResult(
			isWon = isWon,
			isLost = isLost,
			updatedGame = updatedGame
		)
	}
}
