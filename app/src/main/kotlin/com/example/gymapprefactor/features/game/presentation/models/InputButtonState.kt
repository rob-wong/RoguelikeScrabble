package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.common.components.presentation.ImageState

sealed class InputButtonState {
	data class InputWordButton(
		val onClick: (List<String>) -> Unit,
		val image: ImageState,
	) : InputButtonState()

	data object None : InputButtonState()
}
