package com.example.gymapprefactor.features.routines.start.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class StartRoutineState : State {
    data class Content(
        val topBarState: TopBarState,
    ) : StartRoutineState()

    data object None : StartRoutineState()
}
