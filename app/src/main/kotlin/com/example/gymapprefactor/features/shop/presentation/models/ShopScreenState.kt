package com.example.gymapprefactor.features.shop.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class ShopScreenState : State {
	data class Content(
		val resourceBar: ResourceBarState
	) : ShopScreenState()

	data object None : ShopScreenState()
}
