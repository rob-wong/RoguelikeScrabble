package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.business.models.Letter

sealed class MidshopResultPayload {
	data class Upgrade(
		val originalLetters: List<Letter>,
		val upgradedLetters: List<Letter>,
		val glyphsGained: Int
	) : MidshopResultPayload()
	
	// Future types will be added here:
	// data class Awaken(...) : MidshopResultPayload()
	// data class Expunge(...) : MidshopResultPayload()
	// etc.
}
