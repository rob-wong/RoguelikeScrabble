package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.ActiveGameState
import javax.inject.Inject

data class PerfectionismMidshopResult(
	val gameState: ActiveGameState,
	val oldMaxDiscards: Int,
	val newMaxDiscards: Int
)

interface PerfectionismMidshopOptionMapper :
	Mapper<PerfectionismMidshopOptionMapper.Param, PerfectionismMidshopResult> {
	data class Param(
		val game: ActiveGameState
	)
}

class PerfectionismMidshopOptionMapperImpl @Inject constructor() : PerfectionismMidshopOptionMapper {
	companion object {
		private const val DISCARD_INCREASE = 1
	}

	override fun map(param: PerfectionismMidshopOptionMapper.Param): PerfectionismMidshopResult {
		val oldMaxDiscards = param.game.activeGameVariables.maxDiscards
		val newMaxDiscards = oldMaxDiscards + DISCARD_INCREASE
		
		val updatedGameState = param.game.copy(
			activeGameVariables = param.game.activeGameVariables.copy(
				maxDiscards = newMaxDiscards
			)
		)
		
		return PerfectionismMidshopResult(
			gameState = updatedGameState,
			oldMaxDiscards = oldMaxDiscards,
			newMaxDiscards = newMaxDiscards
		)
	}
}
