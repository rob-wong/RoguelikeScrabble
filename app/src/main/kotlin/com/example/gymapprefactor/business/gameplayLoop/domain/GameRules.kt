package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.models.ActiveGameState

interface GameRules {
	fun calculateEnemyDamage(score: Int, game: ActiveGameState): Int

	fun checkWinCondition(game: ActiveGameState): Boolean

	fun checkLossCondition(game: ActiveGameState): Boolean
}
