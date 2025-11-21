package com.example.gymapprefactor.features.game.presentation.models.midshop

import com.example.gymapprefactor.business.models.Letter

sealed class MidshopResultPayload {
	data class Upgrade(
		val originalLetters: List<Letter>,
		val upgradedLetters: List<Letter>,
		val glyphsGained: Int
	) : MidshopResultPayload()
	
	data class Awaken(
		val generatedLetters: List<Letter>
	) : MidshopResultPayload()
	
	data class Expunge(
		val lettersToChooseFrom: List<Letter>
	) : MidshopResultPayload()
	
	data class Perfectionism(
		val oldMaxDiscards: Int,
		val newMaxDiscards: Int
	) : MidshopResultPayload()
	
	data class Persistence(
		val oldMaxRounds: Int,
		val newMaxRounds: Int
	) : MidshopResultPayload()
}
