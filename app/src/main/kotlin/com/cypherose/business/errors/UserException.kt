package com.cypherose.business.errors

sealed class UserException(message: String) : Exception(message) {
	data object UserNotFound : UserException("User not found")
	
	data class InsufficientRunes(
		val required: Int,
		val available: Int
	) : UserException("Insufficient runes. Required: $required, Available: $available")
	
	data class LetterNotFound(
		val letterId: String
	) : UserException("Letter not found in deck: $letterId")
	
	data class DeckNotFound(
		val deckId: String
	) : UserException("Deck not found: $deckId")
}
