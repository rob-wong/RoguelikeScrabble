package com.cypherose.common.components.buttons.presentation

import com.cypherose.common.components.presentation.ImageState

sealed class IconButtonState : ButtonState {
	data class Content(
		val onClick: () -> Unit,
		val image: ImageState,
	) : IconButtonState()

	data object None : IconButtonState()
}
