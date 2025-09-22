package com.example.gymapprefactor.features.shop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenState
import kotlinx.coroutines.flow.Flow

abstract class ShopScreenViewModel : ViewModel() {
	abstract val state: Flow<ShopScreenState>
}
