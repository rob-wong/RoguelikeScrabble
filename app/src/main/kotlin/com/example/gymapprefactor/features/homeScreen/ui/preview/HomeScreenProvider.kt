package com.example.gymapprefactor.features.homeScreen.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState

class HomeScreenProvider : PreviewParameterProvider<HomeScreenState.Content> {
	override val values = sequenceOf(
		HomeScreenState.Content(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceBarState.ResourceState(
					isDisplayed = true,
					amount = "30",
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceBarState.ResourceState(
					isDisplayed = false,
					amount = "0",
					icon = ImageState.None
				)
			),
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
