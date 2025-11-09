package com.example.gymapprefactor.features.game.presentation.models.components

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.presentation.ImageState

sealed class DiscardsRemainingState : State {
	data class Content(
		val image : ImageState,
		val remaining : Int,
	) : DiscardsRemainingState()

	data object None : DiscardsRemainingState()
}
