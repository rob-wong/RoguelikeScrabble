package com.cypherose.features.templateengine.presentation.models.shopcard

import com.cypherose.business.interfaces.Action

sealed class ShopCardItemAction : Action {
	data class SetContent(
		val id: String,
		val price: PriceContent? = null,
		val product: ProductContent? = null,
		val description: String? = null
	) : ShopCardItemAction()

	data object None : ShopCardItemAction()
}
