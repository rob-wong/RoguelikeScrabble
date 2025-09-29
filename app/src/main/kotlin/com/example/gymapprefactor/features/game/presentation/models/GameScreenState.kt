package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class GameScreenState : State {
	data class Playing(
		val resourceBar: ResourceBarState
	): GameScreenState()

	data object None : GameScreenState()
}
