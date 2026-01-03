package com.example.gymapprefactor.features.templateengine.presentation.models.shopcard

import com.example.gymapprefactor.business.interfaces.Action

sealed class ShopCardItemAction : Action {
	data class SetContent(
		val id: String,
		val price: PriceContent? = null,
		val product: ProductContent? = null,
		val description: String? = null
	) : ShopCardItemAction()

	data object None : ShopCardItemAction()
}
