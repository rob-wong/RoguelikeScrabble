package com.cypherose.business.gameplayLoop.domain.mappers

import com.cypherose.business.interfaces.Mapper
import com.cypherose.business.models.ActiveGameState
import javax.inject.Inject

interface DiscardsRemainingMapper : Mapper<DiscardsRemainingMapper.Param, Int> {
	data class Param(
		val game: ActiveGameState
	)
}

class DiscardsRemainingMapperImpl @Inject constructor() : DiscardsRemainingMapper {
	override fun map(param: DiscardsRemainingMapper.Param): Int {
		with(param.game) {
			return activeGameVariables.maxDiscards - currentRound.discardsUsed
		}
	}
}
