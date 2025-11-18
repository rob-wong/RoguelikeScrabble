package com.example.gymapprefactor.features.game.presentation.models

data class MidshopOption(
	val id: String,
	val cost: Int,
	val type: MidshopOptionType,
)

sealed class MidshopOptionType {
	data object Upgrade : MidshopOptionType()
	data object Awaken : MidshopOptionType()
	data object Expunge : MidshopOptionType()
	data object Perfectionism : MidshopOptionType()
	data object Persistence : MidshopOptionType()
}
