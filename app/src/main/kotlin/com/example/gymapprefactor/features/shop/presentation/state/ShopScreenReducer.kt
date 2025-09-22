package com.example.gymapprefactor.features.shop.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenAction
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenState

interface ShopScreenReducer : Reducer<ShopScreenState, ShopScreenAction>
