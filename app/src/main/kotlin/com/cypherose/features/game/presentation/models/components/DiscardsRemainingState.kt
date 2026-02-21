package com.cypherose.features.game.presentation.models.components

import com.cypherose.business.interfaces.State
import com.cypherose.common.components.presentation.ImageState

sealed class DiscardsRemainingState : State {
	data class Content(
		val image : ImageState,
		val remaining : Int,
	) : DiscardsRemainingState()

	data object None : DiscardsRemainingState()
}
