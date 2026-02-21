package com.cypherose.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.state.DialogReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class SettingsRootViewModel : ViewModel() { abstract val state: Flow<SettingsRootState> }

@HiltViewModel
class SettingsRootViewModelImpl @Inject constructor(
	private val dialogReducer: DialogReducer,
	private val dispatcherProvider: DispatcherProvider,
) : SettingsRootViewModel() {
	override val state = MutableStateFlow<SettingsRootState>(SettingsRootState.None)

	init {
		setContent()
	}

	private fun setContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			state.value = SettingsRootState.Content(
				settingsButton = IconButtonState.Content(
					onClick = ::openSettingsDialog,
					image = ImageState.SettingsButton
				)
			)
		}
	}

	private fun openSettingsDialog() {
		viewModelScope.launch(dispatcherProvider.main) {
			dialogReducer.update(DialogAction.TriggerDialog(
				onDismiss = { dialogReducer.onDefaultDismiss() },
				title = "",
				confirmState = DialogAction.ConfirmState.None,
				showDismissButton = true,
			))
		}
	}
}
