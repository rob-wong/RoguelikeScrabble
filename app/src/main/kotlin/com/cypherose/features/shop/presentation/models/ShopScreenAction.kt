package com.cypherose.features.shop.presentation.models

import com.cypherose.business.interfaces.Action
import com.cypherose.business.interfaces.State

sealed class ShopScreenAction : Action {
	data class SetContent(
		val runesCount: Int,
		val onBackPressed: () -> Unit,
		val templateStates: List<State>
	): ShopScreenAction()

	data object None : ShopScreenAction()
}
