package com.example.gymapprefactor.features.exercises.pick.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class PickExerciseState : State {

    data class Content(
        val exerciseList: List<RectangleButtonState>,
        val topBarState: TopBarState,
    ) : PickExerciseState()

    data object None : PickExerciseState()
}
