package com.example.gymapprefactor.features.routines.base.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState

class BaseRoutineScreenProvider : PreviewParameterProvider<BaseRoutineScreenState.Content> {
    override val values = sequenceOf(
        BaseRoutineScreenState.Content(
            routineButtons = listOf(
                RectangleButtonState.Content(onClick = { }, text = "ROUTINE 1"),
                RectangleButtonState.Content(onClick = { }, text = "ROUTINE 2"),
                RectangleButtonState.Content(onClick = { }, text = "ROUTINE 3"),
                RectangleButtonState.None, // 4
                RectangleButtonState.Content(onClick = { }, text = "ROUTINE 5"),

            ),
            createRoutineButton = RectangleButtonState.Content(onClick = { }, text = "create"),
            topBarState = TopBarState.Content(title = "Routines", onBack = { })
        )
    )
}
