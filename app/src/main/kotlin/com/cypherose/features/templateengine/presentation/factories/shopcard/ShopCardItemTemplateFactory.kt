package com.cypherose.features.templateengine.presentation.factories.shopcard

import androidx.lifecycle.ViewModel
import com.cypherose.business.templateengine.domain.models.TemplateItem
import com.cypherose.features.templateengine.presentation.factories.ItemTemplateFactory
import com.cypherose.features.templateengine.presentation.models.shopcard.PriceContent
import com.cypherose.features.templateengine.presentation.models.shopcard.ProductContent
import com.cypherose.features.templateengine.presentation.state.shopcard.ShopCardItemReducerImpl
import com.cypherose.features.templateengine.presentation.viewmodel.shopcard.ShopCardItemViewModelImpl
import kotlinx.coroutines.runBlocking

class ShopCardItemTemplateFactory : ItemTemplateFactory {
	override fun createViewModel(templateItem: TemplateItem): ViewModel {
		val reducer = ShopCardItemReducerImpl()
		val viewModel = ShopCardItemViewModelImpl(reducer)
		runBlocking {
			viewModel.setContent(
				id = templateItem.id,
				price = templateItem.price?.let { 
					PriceContent(type = it.type, amount = it.amount) 
				},
				product = templateItem.product?.let {
					ProductContent(
						type = it.type,
						amount = it.amount,
						decktype = it.decktype
					)
				},
				description = templateItem.description
			)
		}
		return viewModel
	}
}
