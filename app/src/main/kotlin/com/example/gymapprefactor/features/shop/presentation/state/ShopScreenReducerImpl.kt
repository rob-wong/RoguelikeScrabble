package com.example.gymapprefactor.features.shop.presentation.state

import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenAction
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class ShopScreenReducerImpl : ShopScreenReducer {
	override val state = MutableStateFlow<ShopScreenState>(ShopScreenState.None)

	override suspend fun update(action: ShopScreenAction) {
		state.value = when (action) {
			is ShopScreenAction.SetContent -> mapContent(action)
			is ShopScreenAction.None -> ShopScreenState.None
		}
	}

	private fun mapContent(action: ShopScreenAction.SetContent): ShopScreenState {
		return ShopScreenState.Content(
			resourceBar = ResourceBarState.Content(
				ResourceBarState.ResourceState(
					isDisplayed = true,
					amount = action.runesCount.toString(),
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceBarState.ResourceState(
					isDisplayed = false,
					amount = "",
					icon = ImageState.None
				),
			)
		)
	}
}
