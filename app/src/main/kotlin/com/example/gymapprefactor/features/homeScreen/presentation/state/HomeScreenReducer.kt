package com.example.gymapprefactor.features.homeScreen.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState

interface HomeScreenReducer: Reducer<HomeScreenState, HomeScreenAction>
