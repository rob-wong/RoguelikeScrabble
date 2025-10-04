package com.example.gymapprefactor.features.dialogs.presentation.state

import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction.*
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogState
import kotlinx.coroutines.flow.MutableStateFlow

class DialogReducerImpl : DialogReducer {
	override val state = MutableStateFlow<DialogState>(DialogState.None)

	private lateinit var defaultDismiss: () -> Unit

	override suspend fun update(action: DialogAction) {
		state.value = when (action) {
			is InitializeDismiss -> initializeDismiss(action)
			is TriggerDialog -> setDialogContent(action)
			is ClearDialogs -> DialogState.None
			is None -> DialogState.None
		}
	}

	override fun onDefaultDismiss() {
		defaultDismiss()
	}

	private fun setDialogContent(action: TriggerDialog): DialogState {
		return with(action) {
			DialogState.Content(
				onDismissRequest = onDismiss,
				title = title,
				confirmState = mapConfirmButton(action),
				dismissState = mapDismissButton(action)
			)
		}
	}

	private fun initializeDismiss(action: InitializeDismiss): DialogState {
		defaultDismiss = action.onDismiss
		return state.value
	}

	private fun mapConfirmButton(action: TriggerDialog): ButtonState {
		with(action) {
			return when (confirmState) {
				is ConfirmState.Content -> IconButtonState.Content(
					onClick = {
						confirmState.onConfirm()
						action.onDismiss()
					},
					image = ImageState.ConfirmIcon
				)
				is ConfirmState.None -> IconButtonState.None
			}
		}
	}

	private fun mapDismissButton(action: TriggerDialog): ButtonState {
		with(action) {
			return when (showDismissButton) {
				true -> IconButtonState.Content(
					onClick = onDismiss,
					image = ImageState.DismissIcon
				)
				false -> IconButtonState.None
			}
		}
	}
}
