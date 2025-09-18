package com.example.gymapprefactor.common.components.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.FoldingButtonState
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState

class FoldingButtonProvider : PreviewParameterProvider<FoldingButtonState.Content> {
	override val values = sequenceOf(
		FoldingButtonState.Content(
			text = "Folded Button",
			buttons = listOf(
				RectangleButtonState.Content(onClick = { }, text = "Button 1"),
				RectangleButtonState.Content(onClick = { }, text = "Button 2")
			)
		),
		FoldingButtonState.Content(
			text = "Unfolded Button",
			buttons = listOf()
		),
	)
}
