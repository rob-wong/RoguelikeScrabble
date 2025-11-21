package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.PersistenceMidshopOptionMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopResultPayload
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

