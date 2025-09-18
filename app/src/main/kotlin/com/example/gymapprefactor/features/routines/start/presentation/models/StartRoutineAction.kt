package com.example.gymapprefactor.features.routines.start.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class StartRoutineAction : Action {
    data class SetContent(
        val topBarState: TopBarState,
    ) : StartRoutineAction()

    data object None : StartRoutineAction()
}
