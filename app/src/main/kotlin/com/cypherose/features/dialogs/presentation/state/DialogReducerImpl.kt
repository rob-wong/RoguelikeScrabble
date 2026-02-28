package com.cypherose.features.dialogs.presentation.state

import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.models.DialogAction.*
import com.cypherose.features.dialogs.presentation.models.DialogState
import kotlinx.coroutines.flow.MutableStateFlow

class DialogReducerImpl : DialogReducer {
	override val state = MutableStateFlow<DialogState>(DialogState.None)

	private var defaultDismiss: (() -> Unit)? = null

	override suspend fun update(action: DialogAction) {
		state.value = when (action) {
			is InitializeDismiss -> handleInitializeDismiss(action)
			is TriggerDialog -> createDialogState(action)
			is ClearDialogs -> DialogState.None
			is None -> DialogState.None
		}
	}

	override fun onDefaultDismiss() {
		defaultDismiss?.invoke()
	}

	private fun handleInitializeDismiss(action: InitializeDismiss): DialogState {
		defaultDismiss = action.onDismiss
		return state.value
	}

	private fun createDialogState(action: TriggerDialog): DialogState {
		return DialogState.Content(
			onDismissRequest = action.onDismiss,
			title = action.title,
			message = action.message,
			customContent = action.customContent,
			confirmState = createConfirmButton(action),
			dismissState = createDismissButton(action)
		)
	}

	private fun createConfirmButton(action: TriggerDialog): ButtonState {
		return when (val confirm = action.confirmState) {
			is ConfirmState.Content -> IconButtonState.Content(
				onClick = {
					confirm.onConfirm()
					action.onDismiss()
				},
				image = ImageState.Basic.ConfirmIcon
			)
			is ConfirmState.None -> IconButtonState.None
		}
	}

	private fun createDismissButton(action: TriggerDialog): ButtonState {
		return if (action.showDismissButton) {
			IconButtonState.Content(
				onClick = action.onDismiss,
				image = ImageState.Basic.DismissIcon
			)
		} else {
			IconButtonState.None
		}
	}
}
