package com.example.gymapprefactor.features.routines.edit.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineState

class EditRoutineScreenProvider : PreviewParameterProvider<EditRoutineState.Content> {
    private val existingExerciseState = RectangleButtonState.Content(
        onClick = { }, text = "existing exercise 1"
    )
    private val existingExerciseState2 = RectangleButtonState.Content(
        onClick = { }, text = "existing exercise 2"
    )
    override val values = sequenceOf(
        EditRoutineState.Content(
            exerciseList = listOf(existingExerciseState, existingExerciseState2),
            addExerciseButton = RectangleButtonState.Content(
                onClick = { },
                text = "Add Exercise"
            ),
            nameInputField = TextInputFieldState.Content(
                onValueChanged = { },
                text = "Routine Name",
                placeholder = " "
            ),
            saveButton = RectangleButtonState.Content(
                onClick = { },
                text = "Save"
            ),
            topBarState = TopBarState.Content(
                onBack = { },
                title = "Edit Routine"
            )
        ),
        EditRoutineState.Content(
            exerciseList = listOf(existingExerciseState, existingExerciseState2),
            addExerciseButton = RectangleButtonState.Content(
                onClick = { },
                text = "Add Exercise"
            ),
            nameInputField = TextInputFieldState.Content(
                onValueChanged = { },
                text = "Routine Name",
                placeholder = " "
            ),
            saveButton = RectangleButtonState.Content(
                onClick = { },
                text = "Save"
            ),
            topBarState = TopBarState.Content(
                onBack = { },
                title = "Create Routine"
            )
        ),
    )
}
