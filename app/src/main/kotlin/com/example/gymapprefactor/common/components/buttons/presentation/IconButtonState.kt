package com.example.gymapprefactor.common.components.buttons.presentation

import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState

sealed class IconButtonState : ButtonState {
	data class Content(
		val onClick: () -> Unit,
		val image: ImageState,
	) : IconButtonState()

	data object None : IconButtonState()
}