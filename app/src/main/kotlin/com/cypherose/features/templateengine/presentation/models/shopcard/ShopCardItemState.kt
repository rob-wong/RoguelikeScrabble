package com.cypherose.features.templateengine.presentation.models.shopcard

import com.cypherose.business.interfaces.State

sealed class ShopCardItemState : State {
	data class Content(
		val id: String,
		val price: PriceContent? = null,
		val product: ProductContent? = null,
		val description: String? = null
	) : ShopCardItemState()

	data object None : ShopCardItemState()
}

data class PriceContent(
	val type: String,
	val amount: Double
)

data class ProductContent(
	val type: String,
	val amount: Int? = null,
	val decktype: String? = null
)
