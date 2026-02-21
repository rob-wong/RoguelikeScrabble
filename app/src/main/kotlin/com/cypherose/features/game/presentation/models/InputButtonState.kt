package com.cypherose.features.game.presentation.models

import com.cypherose.common.components.presentation.ImageState

sealed class InputButtonState {
	data class InputWordButton(
		val onClick: (List<String>) -> Unit,
		val image: ImageState,
	) : InputButtonState()

	data object None : InputButtonState()
}
