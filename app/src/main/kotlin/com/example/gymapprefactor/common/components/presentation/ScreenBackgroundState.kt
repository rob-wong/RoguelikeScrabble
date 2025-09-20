package com.example.gymapprefactor.common.components.presentation

sealed class ScreenBackgroundState {
	data object Home : ScreenBackgroundState()
	data object None : ScreenBackgroundState()
}
