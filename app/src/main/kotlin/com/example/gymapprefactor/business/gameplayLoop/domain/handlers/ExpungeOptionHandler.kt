package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.ExpungeMidshopOptionMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.models.copy
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopResultPayload
import javax.inject.Inject

class ExpungeOptionHandler @Inject constructor(
	private val expungeMidshopOptionMapper: ExpungeMidshopOptionMapper
) : MidshopOptionHandler {

	override fun execute(
		option: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult {
		val expungeResult = expungeMidshopOptionMapper.map(
			ExpungeMidshopOptionMapper.Param(game = game)
		)
		
		val payload = MidshopResultPayload.Expunge(
			lettersToChooseFrom = expungeResult.lettersToChooseFrom
		)
		
		return MidshopOptionExecutionResult(
			gameState = expungeResult.gameState,
			resultPayload = payload,
			shouldAdvance = false
		)
	}

	override fun confirmSelection(
		selection: Any,
		game: ActiveGameState
	): ActiveGameState {
		require(selection is Letter) { "Expunge option requires Letter selection" }
		
		val selectedLetter = selection
		val updatedDeck = game.activeGameValues.deck.copy(
			letters = game.activeGameValues.deck.letters.filter { it.id != selectedLetter.id }
		)
		
		return game.copy(
			activeGameValues = game.activeGameValues.copy(
				deck = updatedDeck
			)
		)
	}
}
