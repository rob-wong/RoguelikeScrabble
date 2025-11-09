package com.example.gymapprefactor.features.game.presentation.models.components

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.ImageState

sealed class RoundsRemainingState : State {
	data class Content(
		val image: ImageState,
		val currentRound: Int,
		val maxRounds: Int,
	) : RoundsRemainingState()

	data object None : RoundsRemainingState()
}
