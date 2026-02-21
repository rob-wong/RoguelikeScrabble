package com.cypherose.business.gameplayLoop.domain

import com.cypherose.business.models.ActiveGameState
import javax.inject.Inject

class GameRulesImpl @Inject constructor() : GameRules {
	override fun calculateEnemyDamage(score: Int, game: ActiveGameState): Int {
		return score
	}

	override fun checkWinCondition(game: ActiveGameState): Boolean {
		return game.currentRound.enemyHealth <= 0
	}

	override fun checkLossCondition(game: ActiveGameState): Boolean {
		return game.currentRound.round > game.activeGameVariables.maxRounds
	}
}
