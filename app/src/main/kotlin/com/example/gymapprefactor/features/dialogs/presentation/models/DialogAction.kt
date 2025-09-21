package com.example.gymapprefactor.features.dialogs.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class DialogAction : Action {
	data class TriggerDialog(
		val onDismiss: () -> Unit,
		val title: String
	) : DialogAction()

	data object ClearDialogs : DialogAction()

	data object None : DialogAction()
}
