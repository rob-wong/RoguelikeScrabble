package com.example.gymapprefactor.features.dialogs.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.state.DialogReducer
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
			dialogReducer.update(DialogAction.ClearDialogs)
		}
	}
}
