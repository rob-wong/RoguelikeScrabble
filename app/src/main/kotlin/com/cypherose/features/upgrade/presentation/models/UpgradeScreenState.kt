package com.cypherose.features.upgrade.presentation.models

import com.cypherose.business.interfaces.State
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.presentation.ResourceBarState

sealed class UpgradeScreenState : State {
	data class Content(
		val resourceBar: ResourceBarState,
		val backButton: ButtonState,
		val letters: List<UpgradeLetterState>,
		val selectedLetter: SelectedLetterState?,
		val upgradeAnimation: UpgradeAnimationPayload?,
	) : UpgradeScreenState()

	data object None : UpgradeScreenState()
}
