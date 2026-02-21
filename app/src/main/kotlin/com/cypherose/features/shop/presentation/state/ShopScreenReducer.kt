package com.cypherose.features.shop.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.shop.presentation.models.ShopScreenAction
import com.cypherose.features.shop.presentation.models.ShopScreenState

interface ShopScreenReducer : Reducer<ShopScreenState, ShopScreenAction>
