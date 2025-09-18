package com.example.gymapprefactor.features.routines.base.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class BaseRoutineScreenState : State {

    data class Content(
        val routineButtons: List<RectangleButtonState>,
        val createRoutineButton: RectangleButtonState,
        val topBarState: TopBarState,
    ) : BaseRoutineScreenState()

    data object None : BaseRoutineScreenState()
}
