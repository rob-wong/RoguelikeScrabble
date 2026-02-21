package com.cypherose.features.navigation.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.navigation.presentation.models.NavigationAction
import com.cypherose.features.navigation.presentation.models.NavigationState

interface NavigationReducer : Reducer<NavigationState, NavigationAction>
