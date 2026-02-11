package com.example.gymapprefactor.business.errors

sealed class GameplayErrorException(message: String) : Exception(message) {
	data class InsufficientGlyphs(
		val required: Int,
		val available: Int
	) : GameplayErrorException("Insufficient glyphs. Required: $required, Available: $available")
	
	data class MidshopOptionFailed(
		val reason: String
	) : GameplayErrorException("Midshop option failed: $reason")
}
