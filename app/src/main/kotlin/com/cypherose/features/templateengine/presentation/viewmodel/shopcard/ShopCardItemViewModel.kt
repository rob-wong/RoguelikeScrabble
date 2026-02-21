package com.cypherose.features.templateengine.presentation.viewmodel.shopcard

import androidx.lifecycle.ViewModel
import com.cypherose.features.templateengine.presentation.models.shopcard.PriceContent
import com.cypherose.features.templateengine.presentation.models.shopcard.ProductContent
import com.cypherose.features.templateengine.presentation.models.shopcard.ShopCardItemState
import kotlinx.coroutines.flow.Flow

abstract class ShopCardItemViewModel : ViewModel() {
	abstract val state: Flow<ShopCardItemState>
	abstract suspend fun setContent(
		id: String,
		price: PriceContent? = null,
		product: ProductContent? = null,
		description: String? = null
	)
}
