package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopResultPayload

data class MidshopOptionExecutionResult(
	val gameState: ActiveGameState,
	val resultPayload: MidshopResultPayload?,
	val shouldAdvance: Boolean
)

// Each option type implements this interface to encapsulate its execution logic.
interface MidshopOptionHandler {
	fun execute(
		option: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult

	fun confirmSelection(
		selection: Any,
		game: ActiveGameState
	): ActiveGameState? {
		return null
	}
}

