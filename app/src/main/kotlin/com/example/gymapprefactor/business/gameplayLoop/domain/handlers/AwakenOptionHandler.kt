package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.AwakenMidshopOptionMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.models.copy
import com.example.gymapprefactor.features.game.presentation.models.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.MidshopResultPayload
import javax.inject.Inject

class AwakenOptionHandler @Inject constructor(
	private val awakenMidshopOptionMapper: AwakenMidshopOptionMapper
) : MidshopOptionHandler {

	override fun execute(
		option: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult {
		val awakenResult = awakenMidshopOptionMapper.map(
			AwakenMidshopOptionMapper.Param(game = game)
		)
		
		val payload = MidshopResultPayload.Awaken(
			generatedLetters = awakenResult.generatedLetters
		)
		
		return MidshopOptionExecutionResult(
			gameState = awakenResult.gameState,
			resultPayload = payload,
			shouldAdvance = false
		)
	}

	override fun confirmSelection(
		selection: Any,
		game: ActiveGameState
	): ActiveGameState {
		require(selection is Letter) { "Awaken option requires Letter selection" }
		
		val selectedLetter = selection
		val updatedDeck = game.activeGameValues.deck.copy(
			letters = game.activeGameValues.deck.letters + selectedLetter
		)
		
		return game.copy(
			activeGameValues = game.activeGameValues.copy(
				deck = updatedDeck
			)
		)
	}
}

