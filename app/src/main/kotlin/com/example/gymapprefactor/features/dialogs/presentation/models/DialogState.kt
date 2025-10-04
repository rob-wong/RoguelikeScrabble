package com.example.gymapprefactor.features.dialogs.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState

sealed class DialogState : State {
	data class Content(
		val onDismissRequest: () -> Unit,
		val title: String,
		val confirmState: ButtonState,
		val dismissState: ButtonState,
	) : DialogState()

	data object None : DialogState()
}
