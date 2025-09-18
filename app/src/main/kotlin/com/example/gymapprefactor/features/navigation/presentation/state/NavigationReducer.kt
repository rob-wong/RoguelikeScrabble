package com.example.gymapprefactor.features.navigation.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState

interface NavigationReducer : Reducer<NavigationState, NavigationAction>
