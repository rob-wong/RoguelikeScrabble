package com.example.gymapprefactor.common.components.presentation

sealed class ImageState {
	data object SettingsButton : ImageState()
	data object DialogBackground : ImageState()
}
