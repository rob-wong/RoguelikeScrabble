package com.example.gymapprefactor.common.components.buttons.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.buttons.presentation.FoldingButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.RectangleButtonState

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
