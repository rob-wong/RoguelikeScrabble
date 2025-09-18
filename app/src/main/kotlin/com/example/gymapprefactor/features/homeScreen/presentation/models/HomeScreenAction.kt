package com.example.gymapprefactor.features.homeScreen.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class HomeScreenAction: Action {
    data class SetContent(
        val onExercises: ButtonState,
        val onRoutines: ButtonState,
        val onViewStats: ButtonState,
        val topBarState: TopBarState,
    ) : HomeScreenAction()
    data object None : HomeScreenAction()
}
