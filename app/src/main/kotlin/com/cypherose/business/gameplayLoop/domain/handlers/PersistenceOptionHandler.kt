package com.cypherose.business.gameplayLoop.domain.handlers

import com.cypherose.business.gameplayLoop.domain.mappers.PersistenceMidshopOptionMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopResultPayload
import javax.inject.Inject

class PersistenceOptionHandler @Inject constructor(
	private val persistenceMidshopOptionMapper: PersistenceMidshopOptionMapper
) : MidshopOptionHandler {

	override fun execute(
		option: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult {
		val persistenceResult = persistenceMidshopOptionMapper.map(
			PersistenceMidshopOptionMapper.Param(game = game)
		)
		
		val payload = MidshopResultPayload.Persistence(
			oldMaxRounds = persistenceResult.oldMaxRounds,
			newMaxRounds = persistenceResult.newMaxRounds
		)
		
		return MidshopOptionExecutionResult(
			gameState = persistenceResult.gameState,
			resultPayload = payload,
			shouldAdvance = true
		)
	}
}

