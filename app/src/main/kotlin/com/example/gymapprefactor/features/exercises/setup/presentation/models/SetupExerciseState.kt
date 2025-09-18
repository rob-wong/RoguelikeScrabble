package com.example.gymapprefactor.features.exercises.setup.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class SetupExerciseState : State {
    data class Content(
        val exercise: Exercise,
        val onSave: RectangleButtonState,
        val topBarState: TopBarState,
    ) : SetupExerciseState()

    data object None : SetupExerciseState()
}
