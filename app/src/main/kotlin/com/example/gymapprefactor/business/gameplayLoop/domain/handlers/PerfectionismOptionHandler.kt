package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.PerfectionismMidshopOptionMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopResultPayload
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
