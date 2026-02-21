package com.cypherose.common.components.buttons.presentation

import com.cypherose.common.components.presentation.ImageState

sealed class ImageButtonState : ButtonState {
	data class Content(
		val onClick: () -> Unit,
		val background: ImageState,
		val foreground: ImageState,
	) : ImageButtonState()

	data object None : ImageButtonState()
}
