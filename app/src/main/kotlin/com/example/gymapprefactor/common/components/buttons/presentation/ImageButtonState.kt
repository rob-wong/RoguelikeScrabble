package com.example.gymapprefactor.common.components.buttons.presentation

import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState

sealed class ImageButtonState : ButtonState {
	data class Content(
		val onClick: () -> Unit,
		val background: ImageState,
		val foreground: ImageState,
	) : ImageButtonState()

	data object None : ImageButtonState()
}
