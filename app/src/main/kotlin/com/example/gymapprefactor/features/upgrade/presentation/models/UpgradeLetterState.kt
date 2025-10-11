package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.LetterState

sealed class UpgradeLetterState {
	data class Content(
		val letterState: LetterState,
		val buttonState: ButtonState,
	) : UpgradeLetterState()

	data object None : UpgradeLetterState()
}
