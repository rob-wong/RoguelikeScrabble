package com.example.gymapprefactor.features.templateengine.presentation.factories.shopcard

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.data.models.ItemNet
import com.example.gymapprefactor.business.templateengine.domain.registry.ItemTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.state.shopcard.ShopCardItemReducerImpl
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.shopcard.ShopCardItemViewModelImpl

class ShopCardItemTemplateFactory : ItemTemplateFactory {
	override fun createViewModel(itemNet: ItemNet): ViewModel {
		val reducer = ShopCardItemReducerImpl()
		return ShopCardItemViewModelImpl(reducer)
	}
}
