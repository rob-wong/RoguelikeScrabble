package com.cypherose.features.game.presentation.models.components

import com.cypherose.business.interfaces.State
import com.cypherose.common.components.presentation.ImageState

sealed class RoundsRemainingState : State {
	data class Content(
		val image: ImageState,
		val currentRound: Int,
		val maxRounds: Int,
	) : RoundsRemainingState()

	data object None : RoundsRemainingState()
}
