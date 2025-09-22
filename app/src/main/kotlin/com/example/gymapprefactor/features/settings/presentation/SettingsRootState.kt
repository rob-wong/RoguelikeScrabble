package com.example.gymapprefactor.features.settings.presentation

import com.example.gymapprefactor.common.components.presentation.ButtonState

sealed class SettingsRootState {
	data class Content(
		val settingsButton: ButtonState
	) : SettingsRootState()

	data object None : SettingsRootState()
}
