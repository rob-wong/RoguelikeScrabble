package com.cypherose.business.gameplayLoop.domain.mappers

import com.cypherose.business.interfaces.Mapper
import com.cypherose.business.models.ActiveGameState
import javax.inject.Inject

interface GlyphRewardMapper : Mapper<GlyphRewardMapper.Param, Int> {
	data class Param(
		val game: ActiveGameState
	)
}

class GlyphRewardMapperImpl @Inject constructor() : GlyphRewardMapper {
	companion object {
		private const val BOSS_LEVEL_THRESHOLD = 4
		private const val MAX_UNUSED_ROUNDS_REWARD = 3
		private const val MAX_BOSS_BONUS = 10
	}

	override fun map(param: GlyphRewardMapper.Param): Int {
		val unusedRoundsReward = calculateUnusedRoundsReward(param.game)
		val bossBonusReward = calculateBossBonusReward(param.game)
		return unusedRoundsReward + bossBonusReward
	}

	private fun calculateUnusedRoundsReward(game: ActiveGameState): Int {
		val roundWhenEnemyDefeated = game.currentRound.round - 1
		val unusedRounds = game.activeGameVariables.maxRounds - roundWhenEnemyDefeated
		return unusedRounds.coerceAtMost(MAX_UNUSED_ROUNDS_REWARD).coerceAtLeast(0)
	}

	private fun calculateBossBonusReward(game: ActiveGameState): Int {
		if (!isBossLevel(game.activeGameVariables.level)) {
			return 0
		}
		val bonusBasedOnGlyphCount = game.activeGameVariables.glyphCount / 10
		return bonusBasedOnGlyphCount.coerceAtMost(MAX_BOSS_BONUS)
	}

	private fun isBossLevel(level: Int): Boolean {
		return level >= BOSS_LEVEL_THRESHOLD
	}
}

