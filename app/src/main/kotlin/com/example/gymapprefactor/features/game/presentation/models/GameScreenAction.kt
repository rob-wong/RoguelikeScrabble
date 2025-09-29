package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class GameScreenAction : Action {
	data class StartPlaying(
		val glyphCount: Int,
		val runesCount: Int,
	) : GameScreenAction()

	data object None : GameScreenAction()
}
