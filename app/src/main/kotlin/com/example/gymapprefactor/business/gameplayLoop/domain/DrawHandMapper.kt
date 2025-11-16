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

			val debugHandString = (debugHandString ?: DEBUG_HAND_STRING)?.takeIf { it.isNotBlank() }
			val debugResult = debugHandString?.let { 
				tryDrawDebugHand(it, deck.letters, drawnAmount) 
			}

			return debugResult ?: drawRandomHand(deck.letters, seed, drawnAmount)
		}
	}

	private fun drawRandomHand(
		availableLetters: List<Letter>,
		seed: Long,
		requestedAmount: Int
	): Output {
		val random = Random(seed)
		val shuffled = availableLetters.shuffled(random)
		val drawCount = minOf(requestedAmount, shuffled.size)
		val drawn = shuffled.take(drawCount)
		val remaining = shuffled.drop(drawCount)
		return Output(drawn, remaining)
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

		val remainingLetters = availableLetters.toMutableList()
		val drawnLetters = mutableListOf<Letter>()

		for (char in normalizedString) {
			if (!canContinueMatching(drawnLetters.size, requestedAmount, remainingLetters, char)) {
				break
			}

			val matchingLetterIndex = findMatchingLetterIndex(remainingLetters, char)
			val matchedLetter = remainingLetters.removeAt(matchingLetterIndex)
			drawnLetters.add(matchedLetter)
		}

		return drawnLetters.takeIf { it.isNotEmpty() }?.let {
			Output(it, remainingLetters)
		}
	}

	private fun canContinueMatching(
		currentDrawnCount: Int,
		requestedAmount: Int,
		availableLetters: List<Letter>,
		targetChar: Char
	): Boolean {
		if (currentDrawnCount >= requestedAmount) {
			return false
		}
		return findMatchingLetterIndex(availableLetters, targetChar) != -1
	}

	private fun findMatchingLetterIndex(
		availableLetters: List<Letter>,
		targetChar: Char
	): Int {
		return availableLetters.indexOfFirst { 
			it.letter.uppercaseChar() == targetChar.uppercaseChar() 
		}
	}
}
