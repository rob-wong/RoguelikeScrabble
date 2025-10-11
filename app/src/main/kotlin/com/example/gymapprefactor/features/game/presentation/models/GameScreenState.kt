package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class GameScreenState : State {
	data class Playing(
		val resourceBar: ResourceBarState,
		val quitButton: ButtonState,
		val letters: List<LetterState>,
	): GameScreenState()

	data object None : GameScreenState()
}
