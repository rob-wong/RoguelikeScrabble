package com.cypherose.business.gameplayLoop.domain.mappers

import com.cypherose.business.interfaces.Mapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.Letter
import javax.inject.Inject

data class ExpungeMidshopResult(
	val gameState: ActiveGameState,
	val lettersToChooseFrom: List<Letter>
)

interface ExpungeMidshopOptionMapper : Mapper<ExpungeMidshopOptionMapper.Param, ExpungeMidshopResult> {
	data class Param(
		val game: ActiveGameState
	)
}

class ExpungeMidshopOptionMapperImpl @Inject constructor() : ExpungeMidshopOptionMapper {
	companion object {
		private const val LETTERS_TO_SELECT = 5
	}

	override fun map(param: ExpungeMidshopOptionMapper.Param): ExpungeMidshopResult {
		val deck = param.game.activeGameValues.deck
		val selectedLetters = randomlySelectLetters(deck, LETTERS_TO_SELECT)
		
		// Return game state unchanged - the letter will be deleted when user confirms selection
		return ExpungeMidshopResult(
			gameState = param.game,
			lettersToChooseFrom = selectedLetters
		)
	}

	private fun randomlySelectLetters(deck: com.cypherose.business.models.Deck, count: Int): List<Letter> {
		if (deck.letters.isEmpty()) return emptyList()
		
		val shuffled = deck.letters.shuffled()
		return shuffled.take(count.coerceAtMost(shuffled.size))
	}
}
