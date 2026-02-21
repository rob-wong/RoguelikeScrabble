package com.cypherose.features.shop.presentation.models

import com.cypherose.business.interfaces.State
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.presentation.ResourceBarState

sealed class ShopScreenState : State {
	data class Content(
		val resourceBar: ResourceBarState,
		val backButton: ButtonState,
		val templateStates: List<State>
	) : ShopScreenState()

	data object None : ShopScreenState()
}
