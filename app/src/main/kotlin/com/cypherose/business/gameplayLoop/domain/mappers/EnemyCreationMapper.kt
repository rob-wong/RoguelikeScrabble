package com.cypherose.business.gameplayLoop.domain.mappers

import com.cypherose.business.interfaces.Mapper
import javax.inject.Inject

interface EnemyCreationMapper : Mapper<EnemyCreationMapper.Param, Int> {
	data class Param(
		val stage: Int,
		val level: Int,
	)
}

class EnemyCreationMapperImpl @Inject constructor() : EnemyCreationMapper {
	companion object {
		// Base health calculated so that Stage 1, Level 1 = 30
		// Formula: baseHealth * (stage^2.0) * (1 + level * 0.3)
		// 30 = baseHealth * (1^2.0) * (1 + 1 * 0.3)
		// 30 = baseHealth * 1.3
		// baseHealth = 30 / 1.3
		private const val BASE_HEALTH = 30.0 / 1.3
		private const val STAGE_POWER = 2.0
		private const val LEVEL_MULTIPLIER = 0.3
	}

	override fun map(param: EnemyCreationMapper.Param): Int {
		with(param) {
			// Formula: baseHealth * (stage^2.0) * (1 + level * 0.3)
			val stageMultiplier = Math.pow(stage.toDouble(), STAGE_POWER)
			val levelMultiplier = 1 + (level * LEVEL_MULTIPLIER)
			val health = BASE_HEALTH * stageMultiplier * levelMultiplier
			return health.toInt()
		}
	}
}

