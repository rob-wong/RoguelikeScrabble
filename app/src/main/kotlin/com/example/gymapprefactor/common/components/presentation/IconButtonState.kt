package com.example.gymapprefactor.common.components.presentation

sealed class IconButtonState : ButtonState {
	data class Content(
		val onClick: () -> Unit,
		val image: ImageState,
	) : IconButtonState()

	data object None : IconButtonState()
}
