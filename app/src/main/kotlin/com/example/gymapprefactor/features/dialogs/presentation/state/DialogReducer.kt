package com.example.gymapprefactor.features.dialogs.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogState

interface DialogReducer : Reducer <DialogState, DialogAction> {
	fun onDefaultDismiss()
}
