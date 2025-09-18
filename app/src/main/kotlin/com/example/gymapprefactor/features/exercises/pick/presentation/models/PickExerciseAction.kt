package com.example.gymapprefactor.features.exercises.pick.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class PickExerciseAction : Action {

    data class SetContent(
        val exerciseList: List<RectangleButtonState>,
        val topBarState: TopBarState,
    ) : PickExerciseAction()

    data object None : PickExerciseAction()
}
