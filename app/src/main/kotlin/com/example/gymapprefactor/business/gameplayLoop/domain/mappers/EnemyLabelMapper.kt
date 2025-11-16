package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import javax.inject.Inject

interface EnemyLabelMapper : Mapper<EnemyLabelMapper.Param, String> {
	data class Param(
		val level: Int
	)
}

class EnemyLabelMapperImpl @Inject constructor() : EnemyLabelMapper {
	companion object {
		private const val BOSS_LEVEL_THRESHOLD = 4
		private const val BOSS_LABEL = "BOSS"
		private const val ENEMY_LABEL = "ENEMY"
	}

	override fun map(param: EnemyLabelMapper.Param): String {
		return if (param.level >= BOSS_LEVEL_THRESHOLD) {
			BOSS_LABEL
		} else {
			ENEMY_LABEL
		}
	}
}
