package com.cypherose.features.dialogs.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.models.DialogState

interface DialogReducer : Reducer <DialogState, DialogAction> {
	fun onDefaultDismiss()
}
