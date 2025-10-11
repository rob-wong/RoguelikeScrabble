package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class UpgradeScreenState : State {
	data class Content(
		val resourceBar: ResourceBarState,
		val backButton: ButtonState,
		val letters: List<UpgradeLetterState>,
	) : UpgradeScreenState()

	data object None : UpgradeScreenState()
}
