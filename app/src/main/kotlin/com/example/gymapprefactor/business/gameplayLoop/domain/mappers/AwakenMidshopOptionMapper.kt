package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.DefaultLetter
import com.example.gymapprefactor.business.models.Letter
import kotlin.random.Random
import java.util.UUID
import javax.inject.Inject

data class AwakenMidshopResult(
	val gameState: ActiveGameState,
	val generatedLetters: List<Letter>
)

interface AwakenMidshopOptionMapper : Mapper<AwakenMidshopOptionMapper.Param, AwakenMidshopResult> {
	data class Param(
		val game: ActiveGameState
	)
}

class AwakenMidshopOptionMapperImpl @Inject constructor() : AwakenMidshopOptionMapper {
	companion object {
		private const val LETTERS_TO_GENERATE = 5
		private val ALL_LETTERS = ('a'..'z').toList()
	}

	override fun map(param: AwakenMidshopOptionMapper.Param): AwakenMidshopResult {
		val deck = param.game.activeGameValues.deck
		val averageLevel = calculateAverageLevel(deck.letters)
		val generatedLetters = generateRandomLetters(averageLevel, LETTERS_TO_GENERATE)
		
		// Return game state unchanged - the letter will be added when user confirms selection
		return AwakenMidshopResult(
			gameState = param.game,
			generatedLetters = generatedLetters
		)
	}

	private fun calculateAverageLevel(letters: List<Letter>): Double {
		if (letters.isEmpty()) return 1.0
		return letters.map { it.level }.average()
	}

	private fun generateRandomLetters(averageLevel: Double, count: Int): List<Letter> {
		val floorLevel = averageLevel.toInt()
		val ceilLevel = floorLevel + 1
		val fractional = averageLevel - floorLevel

		val selectedChars = ALL_LETTERS.shuffled().take(count)
		
		return selectedChars.map { char ->
			// Use probabilistic level calculation
			val finalLevel = if (Random.nextFloat() < fractional) {
				ceilLevel
			} else {
				floorLevel
			}.coerceIn(1, 5)
			
			DefaultLetter(
				id = "awaken-${UUID.randomUUID()}",
				letter = char,
				level = finalLevel
			)
		}
	}
}


