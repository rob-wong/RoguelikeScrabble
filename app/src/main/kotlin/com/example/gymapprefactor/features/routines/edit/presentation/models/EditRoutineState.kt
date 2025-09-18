package com.example.gymapprefactor.features.routines.edit.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.common.components.presentation.TopBarState

sealed class EditRoutineState : State {
    data class Content(
        val nameInputField: TextInputFieldState,
        val exerciseList: List<ButtonState>,
        val addExerciseButton: ButtonState,
        val saveButton: ButtonState,
        val topBarState: TopBarState,
    ) : EditRoutineState()

    data object None : EditRoutineState()
}
