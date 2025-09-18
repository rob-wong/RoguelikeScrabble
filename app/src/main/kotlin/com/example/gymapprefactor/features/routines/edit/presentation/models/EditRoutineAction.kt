package com.example.gymapprefactor.features.routines.edit.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.common.components.presentation.TopBarState


sealed class EditRoutineAction : Action{
    data class SetContent(
        val topBarState: TopBarState,
        val nameInputField: TextInputFieldState,
        val exerciseSetupList: List<ButtonState>,
        val onClickAddExercise: () -> Unit,
        val onSave: () -> Unit,
    ) : EditRoutineAction()

    data object None : EditRoutineAction()
}
