package com.cypherose.common.components.presentation

import com.cypherose.business.interfaces.State

sealed class ScreenBackgroundState : State {
	data object Home : ScreenBackgroundState()
	data object None : ScreenBackgroundState()
	data object Shop : ScreenBackgroundState()
	data object Upgrade : ScreenBackgroundState()
	data object Game : ScreenBackgroundState()
}
