package com.example.gymapprefactor.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.common.components.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class SettingsRootViewModel : ViewModel() { abstract val state: Flow<SettingsRootState> }

@HiltViewModel
class SettingsRootViewModelImpl @Inject constructor(
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
					onClick = { },
					image = ImageState.SettingsButton
				)
			)
		}
	}
}
