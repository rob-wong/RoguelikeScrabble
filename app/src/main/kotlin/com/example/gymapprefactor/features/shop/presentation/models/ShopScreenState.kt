package com.example.gymapprefactor.features.shop.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class ShopScreenState : State {
	data class Content(
		val resourceBar: ResourceBarState,
		val backButton: ButtonState,
		val templateStates: List<State>
	) : ShopScreenState()

	data object None : ShopScreenState()
}
