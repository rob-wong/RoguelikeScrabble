package com.cypherose.features.settings.presentation

import com.cypherose.common.components.buttons.presentation.ButtonState

sealed class SettingsRootState {
	data class Content(
		val settingsButton: ButtonState
	) : SettingsRootState()

	data object None : SettingsRootState()
}
