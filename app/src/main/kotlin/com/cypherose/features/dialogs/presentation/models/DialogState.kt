package com.cypherose.features.dialogs.presentation.models

import com.cypherose.business.interfaces.State
import com.cypherose.common.components.buttons.presentation.ButtonState

sealed class DialogState : State {
	data class Content(
		val onDismissRequest: () -> Unit,
		val title: String,
		val message: String? = null,
		val customContent: List<State> = emptyList(),
		val confirmState: ButtonState,
		val dismissState: ButtonState,
	) : DialogState()

	data object None : DialogState()
}
