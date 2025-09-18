package com.example.gymapprefactor.features.homeScreen.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState

class HomeScreenProvider : PreviewParameterProvider<HomeScreenState.Content> {
    override val values = sequenceOf(
        HomeScreenState.Content(
            onExercises = RectangleButtonState.Content(
                onClick = { },
                text = "Exercises"
            ),
            onRoutines = RectangleButtonState.Content(
                onClick = { },
                text = "Routines",
            ),
            onViewStats = RectangleButtonState.Content(
                onClick = { },
                text = "View Stats"
            ),
            topBarState = TopBarState.Content(title = "Home Screen", onBack = { })
        )
    )
}
