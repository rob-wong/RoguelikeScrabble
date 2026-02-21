package com.cypherose.features.templateengine.presentation.state.shopcard

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.templateengine.presentation.models.shopcard.ShopCardItemAction
import com.cypherose.features.templateengine.presentation.models.shopcard.ShopCardItemState

interface ShopCardItemReducer : Reducer<ShopCardItemState, ShopCardItemAction>
