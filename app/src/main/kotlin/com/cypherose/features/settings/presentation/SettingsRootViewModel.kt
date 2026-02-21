package com.cypherose.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypherose.BuildConfig
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.business.interfaces.State
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.state.DialogReducer
import com.cypherose.features.templateengine.presentation.models.settings.AppInfoItemState
import com.cypherose.features.templateengine.presentation.models.settings.ExternalLinkState
import com.cypherose.features.templateengine.presentation.models.settings.SoundEffectsState
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
			val customContent = buildSettingsContent()
			dialogReducer.update(DialogAction.TriggerDialog(
				onDismiss = { dialogReducer.onDefaultDismiss() },
				title = "Settings",
				confirmState = DialogAction.ConfirmState.None,
				showDismissButton = true,
				customContent = customContent
			))
		}
	}

	private fun buildSettingsContent(): List<State> {
		return listOf(
			AppInfoItemState.Content("App Name", "Cypherose"),
			AppInfoItemState.Content("Version", BuildConfig.VERSION_NAME),
			AppInfoItemState.Content("Version Code", BuildConfig.VERSION_CODE.toString()),
			AppInfoItemState.Content("Package", BuildConfig.APPLICATION_ID),

			SoundEffectsState.Content(
				volume = 0.5f,
				volumeLabel = "Volume: ",
				placeholderText = "(Placeholder - Coming Soon)"
			),

			ExternalLinkState.Content("Privacy Policy", "Coming Soon"),
			ExternalLinkState.Content("Terms of Service", "Coming Soon")
		)
	}
}
