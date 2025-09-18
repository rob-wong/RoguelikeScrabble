package com.example.gymapprefactor.features.homeScreen.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class HomeScreenState: State {

    data class Content (
        val onExercises: ButtonState,
        val onRoutines: ButtonState,
        val onViewStats: ButtonState,
        val topBarState: TopBarState,
    ): HomeScreenState()

    data object None : HomeScreenState()
}
