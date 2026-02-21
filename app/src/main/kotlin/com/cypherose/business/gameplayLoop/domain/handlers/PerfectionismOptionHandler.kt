package com.cypherose.business.gameplayLoop.domain.handlers

import com.cypherose.business.gameplayLoop.domain.mappers.PerfectionismMidshopOptionMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopResultPayload
import javax.inject.Inject

class PerfectionismOptionHandler @Inject constructor(
	private val perfectionismMidshopOptionMapper: PerfectionismMidshopOptionMapper
) : MidshopOptionHandler {

	override fun execute(
		option: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult {
		val perfectionismResult = perfectionismMidshopOptionMapper.map(
			PerfectionismMidshopOptionMapper.Param(game = game)
		)
		
		val payload = MidshopResultPayload.Perfectionism(
			oldMaxDiscards = perfectionismResult.oldMaxDiscards,
			newMaxDiscards = perfectionismResult.newMaxDiscards
		)
		
		return MidshopOptionExecutionResult(
			gameState = perfectionismResult.gameState,
			resultPayload = payload,
			shouldAdvance = true
		)
	}
}
