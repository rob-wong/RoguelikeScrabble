package com.example.gymapprefactor.features.templateengine.presentation.state.shopcard

import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemAction
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemState
import kotlinx.coroutines.flow.MutableStateFlow

class ShopCardItemReducerImpl : ShopCardItemReducer {
	override val state = MutableStateFlow<ShopCardItemState>(ShopCardItemState.None)

	override suspend fun update(action: ShopCardItemAction) {
		state.value = when (action) {
			is ShopCardItemAction.SetContent -> ShopCardItemState.Content(
				id = action.id,
				price = action.price,
				product = action.product,
				description = action.description
			)
			is ShopCardItemAction.None -> ShopCardItemState.None
		}
	}
}
