package com.example.gymapprefactor.features.game.presentation.models.midshop

data class MidshopOption(
	val id: String,
	val cost: Int,
	val type: MidshopOptionType,
	val isEnabled: Boolean = true,
)

sealed class MidshopOptionType {
	data object Upgrade : MidshopOptionType()
	data object Awaken : MidshopOptionType()
	data object Expunge : MidshopOptionType()
	data object Perfectionism : MidshopOptionType()
	data object Persistence : MidshopOptionType()
}
