package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.models.ActiveGameState
import javax.inject.Inject

data class GameConditionResult(
	val isWon: Boolean,
	val isLost: Boolean,
	val updatedGame: ActiveGameState
)

class CheckGameConditionsUseCase @Inject constructor(
	private val gameRules: GameRules
) {
	operator fun invoke(game: ActiveGameState): GameConditionResult {
		val isWon = gameRules.checkWinCondition(game)
		val isLost = gameRules.checkLossCondition(game) && !isWon

		return GameConditionResult(
			isWon = isWon,
			isLost = isLost,
			updatedGame = game.copy(
				activeGameVariables = game.activeGameVariables.copy(
					gameLost = isLost
				)
			)
		)
	}
}
