package com.example.gymapprefactor.features.dialogs.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class DialogAction : Action {

	data class InitializeDismiss(
		val onDismiss: () -> Unit
	) : DialogAction()
	data class TriggerDialog( // could do list of states in ui refinement, game lost icon
		val onDismiss: () -> Unit,
		val showDismissButton: Boolean,
		val title: String,
		val message: String? = null,
		val confirmState: ConfirmState
	) : DialogAction()

	data object ClearDialogs : DialogAction()

	data object None : DialogAction()

	sealed class ConfirmState {
		data class Content(
			val onConfirm: () -> Unit
		): ConfirmState()

		data object None : ConfirmState()
	}
}
