package com.cypherose.features.shop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cypherose.features.shop.presentation.models.ShopScreenState
import kotlinx.coroutines.flow.Flow

abstract class ShopScreenViewModel : ViewModel() {
	abstract val state: Flow<ShopScreenState>
}
