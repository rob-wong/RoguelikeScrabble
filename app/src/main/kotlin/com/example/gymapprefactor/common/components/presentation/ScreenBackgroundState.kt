package com.example.gymapprefactor.common.components.presentation

sealed class ScreenBackgroundState {
	data object Home : ScreenBackgroundState()
	data object None : ScreenBackgroundState()
	data object Shop : ScreenBackgroundState()
	data object Upgrade : ScreenBackgroundState()
}
