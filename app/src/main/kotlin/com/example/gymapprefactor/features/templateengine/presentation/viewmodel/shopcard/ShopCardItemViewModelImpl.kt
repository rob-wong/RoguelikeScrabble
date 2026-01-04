package com.example.gymapprefactor.features.templateengine.presentation.viewmodel.shopcard

import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.PriceContent
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ProductContent
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemAction
import com.example.gymapprefactor.features.templateengine.presentation.state.shopcard.ShopCardItemReducer

class ShopCardItemViewModelImpl(
	private val shopCardItemReducer: ShopCardItemReducer
) : ShopCardItemViewModel() {
	override val state = shopCardItemReducer.state

	override suspend fun setContent(
		id: String,
		price: PriceContent?,
		product: ProductContent?,
		description: String?
	) {
		shopCardItemReducer.update(
			ShopCardItemAction.SetContent(
				id = id,
				price = price,
				product = product,
				description = description
			)
		)
	}
}
