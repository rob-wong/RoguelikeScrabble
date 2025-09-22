package com.example.gymapprefactor.features.dialogs.presentation.state

import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogState
import kotlinx.coroutines.flow.MutableStateFlow

class DialogReducerImpl : DialogReducer {
	override val state = MutableStateFlow<DialogState>(DialogState.None)

	override suspend fun update(action: DialogAction) {
		state.value = when (action) {
			is DialogAction.TriggerDialog -> setDialogContent(action)
			is DialogAction.ClearDialogs -> DialogState.None
			is DialogAction.None -> DialogState.None
		}
	}

	private fun setDialogContent(action: DialogAction.TriggerDialog): DialogState {
		return with(action) {
			DialogState.Content(onDismiss, title)
		}
	}
}
