package com.cypherose.features.dialogs.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.state.DialogReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogViewModelImpl @Inject constructor(
	private val dialogReducer: DialogReducer,
	private val dispatcherProvider: DispatcherProvider
) : DialogViewModel() {
	override val state = dialogReducer.state

	init {
		setContent()
	}

	private fun setContent() {
		viewModelScope.launch(dispatcherProvider.main) {
			dialogReducer.update(DialogAction.InitializeDismiss(
				onDismiss = ::dismissDialog
			))
		}
	}

	private fun dismissDialog() {
		viewModelScope.launch(dispatcherProvider.main) {
			dialogReducer.update(DialogAction.ClearDialogs)
		}
	}
}
