package com.example.gymapprefactor.features.shop.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.interfaces.State

sealed class ShopScreenAction : Action {
	data class SetContent(
		val runesCount: Int,
		val onBackPressed: () -> Unit,
		val templateStates: List<State>
	): ShopScreenAction()

	data object None : ShopScreenAction()
}
