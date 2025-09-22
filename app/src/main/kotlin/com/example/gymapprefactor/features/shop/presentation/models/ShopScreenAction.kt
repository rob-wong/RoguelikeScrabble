package com.example.gymapprefactor.features.shop.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class ShopScreenAction : Action {
	data class SetContent(
		val runesCount: Int
	): ShopScreenAction()

	data object None : ShopScreenAction()
}
