package com.example.gymapprefactor.features.exercises.setup.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class SetupExerciseAction : Action {
    data class SetContent(
        val exercise: Exercise,
        val onSave: RectangleButtonState,
        val topBarState: TopBarState,
    ) : SetupExerciseAction()

    data object None : SetupExerciseAction()
}
