package com.example.gymapprefactor.features.game.presentation.models

sealed class MidshopOptionType {
	data object Upgrade : MidshopOptionType()
	data object Awaken : MidshopOptionType()
	data object Expunge : MidshopOptionType()
	data object Perfectionism : MidshopOptionType()
	data object Persistence : MidshopOptionType()
}
