package com.example.gymapprefactor.business.gameplayLoop.domain.usecases

import com.example.gymapprefactor.business.gameplayLoop.domain.GameRules
import com.example.gymapprefactor.business.models.ActiveGameState
import javax.inject.Inject

class ApplyScoreToEnemyUseCase @Inject constructor(
	private val gameRules: GameRules
) {
	operator fun invoke(
		score: Int,
		game: ActiveGameState
	): ActiveGameState {
		val damage = gameRules.calculateEnemyDamage(score, game)
		return game.copy(
			currentRound = game.currentRound.copy(
				enemyHealth = (game.currentRound.enemyHealth - damage).coerceAtLeast(0)
			)
		)
	}
}
