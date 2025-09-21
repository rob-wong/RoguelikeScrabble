package com.example.gymapprefactor.features.dialogs.presentation.models

import com.example.gymapprefactor.business.interfaces.State

sealed class DialogState : State {
	data class Content(
		val onDismissRequest: () -> Unit,
		val title: String,
	) : DialogState()

	data object None : DialogState()
}
