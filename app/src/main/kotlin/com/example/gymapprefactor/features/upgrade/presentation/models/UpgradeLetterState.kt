package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.common.components.presentation.LetterState

sealed class UpgradeLetterState {
	data class Content(
		val letterState: LetterState,
		val cost: Int,
		val canAfford: Boolean,
		val isClickable: Boolean,
		val onLetterClick: (cost: Int) -> Unit,
	) : UpgradeLetterState()

	data object None : UpgradeLetterState()
}
