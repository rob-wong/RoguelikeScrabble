package com.cypherose.features.homeScreen.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.homeScreen.presentation.models.HomeScreenAction
import com.cypherose.features.homeScreen.presentation.models.HomeScreenState

interface HomeScreenReducer: Reducer<HomeScreenState, HomeScreenAction>
