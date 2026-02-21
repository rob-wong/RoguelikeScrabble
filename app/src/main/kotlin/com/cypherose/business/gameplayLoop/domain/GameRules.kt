package com.cypherose.business.gameplayLoop.domain

import com.cypherose.business.models.ActiveGameState

interface GameRules {
	fun calculateEnemyDamage(score: Int, game: ActiveGameState): Int

	fun checkWinCondition(game: ActiveGameState): Boolean

	fun checkLossCondition(game: ActiveGameState): Boolean
}
