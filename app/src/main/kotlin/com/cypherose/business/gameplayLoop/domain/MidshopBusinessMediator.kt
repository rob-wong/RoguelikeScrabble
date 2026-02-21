package com.cypherose.business.gameplayLoop.domain

import com.cypherose.business.gameplayLoop.domain.handlers.MidshopOptionExecutionResult
import com.cypherose.business.gameplayLoop.domain.handlers.MidshopOptionHandlerRegistry
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.Letter
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopOptionType
import javax.inject.Inject

class MidshopBusinessMediator @Inject constructor(
	private val handlerRegistry: MidshopOptionHandlerRegistry,
) {
	fun executeMidshopOption(
		midshopOption: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult {
		val handler = handlerRegistry.getHandler(midshopOption.type)
			?: return MidshopOptionExecutionResult(
				gameState = game,
				resultPayload = null,
				shouldAdvance = true
			)
		
		return handler.execute(midshopOption, game)
	}

	fun confirmAwakenLetterSelection(
		selectedLetter: Letter,
		game: ActiveGameState
	): ActiveGameState {
		val handler = handlerRegistry.getHandler(MidshopOptionType.Awaken)
			?: return game
		
		return handler.confirmSelection(selectedLetter, game) ?: game
	}

	fun confirmExpungeLetterSelection(
		selectedLetter: Letter,
		game: ActiveGameState
	): ActiveGameState {
		val handler = handlerRegistry.getHandler(MidshopOptionType.Expunge)
			?: return game
		
		return handler.confirmSelection(selectedLetter, game) ?: game
	}
}
