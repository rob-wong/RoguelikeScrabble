package com.example.gymapprefactor.features.templateengine.presentation.state.shopcard

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemAction
import com.example.gymapprefactor.features.templateengine.presentation.models.shopcard.ShopCardItemState

interface ShopCardItemReducer : Reducer<ShopCardItemState, ShopCardItemAction>
