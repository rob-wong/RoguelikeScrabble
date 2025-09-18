package com.example.gymapprefactor.features.exercises.pick.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseState

class PickExerciseScreenProvider : PreviewParameterProvider<PickExerciseState.Content> {
    override val values = sequenceOf(
        PickExerciseState.Content(
            exerciseList = listOf(
                RectangleButtonState.Content(onClick = { }, text = "EXERCISE 1"),
                RectangleButtonState.Content(onClick = { }, text = "EXERCISE 2"),
            ),
            topBarState = TopBarState.Content(
                onBack = { },
                title = "Pick Exercise"
            )
        )
    )
}
