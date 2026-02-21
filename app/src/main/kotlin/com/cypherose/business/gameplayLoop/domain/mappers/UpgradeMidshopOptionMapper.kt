package com.cypherose.business.gameplayLoop.domain.mappers

import com.cypherose.business.interfaces.Mapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.Deck
import com.cypherose.business.models.Letter
import com.cypherose.business.models.copy
import javax.inject.Inject

data class UpgradeMidshopResult(
	val gameState: ActiveGameState,
	val originalLetters: List<Letter>,
	val upgradedLetters: List<Letter>,
	val glyphsGained: Int
)

interface UpgradeMidshopOptionMapper : Mapper<UpgradeMidshopOptionMapper.Param, UpgradeMidshopResult> {
	data class Param(
		val game: ActiveGameState
	)
}

class UpgradeMidshopOptionMapperImpl @Inject constructor() : UpgradeMidshopOptionMapper {
	companion object {
		private const val MAX_LETTER_LEVEL = 5
		private const val LETTERS_TO_UPGRADE = 3
	}

	override fun map(param: UpgradeMidshopOptionMapper.Param): UpgradeMidshopResult {
		val deck = param.game.activeGameValues.deck
		val selectedLetters = randomlySelectLetters(deck, LETTERS_TO_UPGRADE)
		
		val upgradeResult = upgradeLetters(
			deck = deck,
			selectedLetters = selectedLetters
		)
		val upgradedDeck = upgradeResult.first
		val originalLetters = upgradeResult.second
		val upgradedLetters = upgradeResult.third
		val glyphsGained = upgradeResult.fourth
		
		val updatedGameState = param.game.copy(
			activeGameValues = param.game.activeGameValues.copy(
				deck = upgradedDeck
			),
			activeGameVariables = param.game.activeGameVariables.copy(
				glyphCount = param.game.activeGameVariables.glyphCount + glyphsGained
			)
		)
		
		return UpgradeMidshopResult(
			gameState = updatedGameState,
			originalLetters = originalLetters,
			upgradedLetters = upgradedLetters,
			glyphsGained = glyphsGained
		)
	}

	private fun randomlySelectLetters(deck: Deck, count: Int): List<Letter> {
		if (deck.letters.isEmpty()) return emptyList()
		
		val shuffled = deck.letters.shuffled()
		return shuffled.take(count.coerceAtMost(shuffled.size))
	}

	private fun upgradeLetters(
		deck: Deck,
		selectedLetters: List<Letter>
	): Quadruple<Deck, List<Letter>, List<Letter>, Int> {
		var glyphsGained = 0
		val originalLetters = mutableListOf<Letter>()
		val upgradedLetters = mutableListOf<Letter>()
		
		val updatedDeckLetters = deck.letters.map { letter ->
			val selectedLetter = selectedLetters.find { it.id == letter.id }
			if (selectedLetter != null) {
				if (letter.level < MAX_LETTER_LEVEL) {
					originalLetters.add(letter)
					val upgraded = letter.copy(level = letter.level + 1)
					upgradedLetters.add(upgraded)
					upgraded
				} else {
					// Letter is at max level, gain a glyph instead
					glyphsGained++
					originalLetters.add(letter)
					upgradedLetters.add(letter) // Add original letter to show in animation
					letter
				}
			} else {
				letter
			}
		}
		
		val updatedDeck = deck.copy(letters = updatedDeckLetters)
		return Quadruple(updatedDeck, originalLetters, upgradedLetters, glyphsGained)
	}
	
	private data class Quadruple<A, B, C, D>(
		val first: A,
		val second: B,
		val third: C,
		val fourth: D
	)
}
