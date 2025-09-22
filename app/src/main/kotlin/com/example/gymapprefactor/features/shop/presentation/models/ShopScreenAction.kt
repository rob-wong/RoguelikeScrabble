package com.example.gymapprefactor.features.shop.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class ShopScreenAction : Action {
	data class SetContent(
		val runesCount: Int,
		val onBackPressed: () -> Unit
	): ShopScreenAction()

	data object None : ShopScreenAction()
}
