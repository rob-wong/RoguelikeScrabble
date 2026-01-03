package com.example.gymapprefactor.features.templateengine.presentation.viewmodel.shopcard

import com.example.gymapprefactor.features.templateengine.presentation.state.shopcard.ShopCardItemReducer

class ShopCardItemViewModelImpl(
	private val shopCardItemReducer: ShopCardItemReducer
) : ShopCardItemViewModel() {
	override val state = shopCardItemReducer.state
}
