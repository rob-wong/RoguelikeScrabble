package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapper.Param
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapper.Output
import kotlin.random.Random

interface DrawHandMapper : Mapper<Param, Output> {
	data class Param(
		val deck: Deck,
		val seed: Long,
		val drawnAmount: Int,
		val debugHandString: String? = null, // Debug: if provided, tries to draw these letters from deck
	)
	data class Output(
		val drawn: List<Letter>,
		val remaining: List<Letter>,
	)
}

class DrawHandMapperImpl : DrawHandMapper {
	
	companion object {
		/**
		 * Debug entry point: Set this string to always draw these specific letters if they exist in the deck.
		 * Set to null or empty string to disable debug mode and use normal random drawing.
		 * Example: "CAT" will try to draw C, A, T from the deck.
		 */
		@JvmStatic
		var DEBUG_HAND_STRING: String? = "DOUBLE"
	}

	override fun map(param: Param): Output {
		with (param) {
			if (deck.letters.isEmpty() || drawnAmount <= 0) {
				return Output(emptyList(), deck.letters)
			}

			// Debug mode: try to draw specific letters if debug string is provided
			// Check both the param and the companion object for debug string
			val debugHandString = (debugHandString ?: DEBUG_HAND_STRING)?.takeIf { it.isNotBlank() }
			val debugResult = debugHandString?.let { 
				tryDrawDebugHand(it, deck.letters, drawnAmount) 
			}
			
			// Use debug result if available, otherwise use normal random drawing
			return debugResult ?: run {
				val random = Random(seed)
				val shuffled = deck.letters.shuffled(random)
				val drawCount = minOf(drawnAmount, shuffled.size)
				val drawn = shuffled.take(drawCount)
				val remaining = shuffled.drop(drawCount)
				Output(drawn, remaining)
			}
		}
	}

	private fun tryDrawDebugHand(
		debugString: String,
		availableLetters: List<Letter>,
		requestedAmount: Int
	): Output? {
		val normalizedString = debugString.uppercase().trim()
		if (normalizedString.isEmpty()) {
			return null
		}

		// Create a mutable copy of available letters to track what we've used
		val remainingLetters = availableLetters.toMutableList()
		val drawnLetters = mutableListOf<Letter>()

		// Try to match each character in the debug string
		for (char in normalizedString) {
			// Stop if we've drawn enough or can't find a matching letter
			val matchingLetterIndex = if (drawnLetters.size < requestedAmount) {
				remainingLetters.indexOfFirst { 
					it.letter.uppercaseChar() == char.uppercaseChar() 
				}
			} else {
				-1
			}

			if (matchingLetterIndex == -1) {
				// If we can't find a letter or have drawn enough, stop trying to match
				break
			}
			
			val matchedLetter = remainingLetters.removeAt(matchingLetterIndex)
			drawnLetters.add(matchedLetter)
		}

		// Return what we have if we drew any letters
		// This allows partial matches if the string is shorter than requested amount
		return drawnLetters.takeIf { it.isNotEmpty() }?.let {
			Output(it, remainingLetters)
		}
	}
}
