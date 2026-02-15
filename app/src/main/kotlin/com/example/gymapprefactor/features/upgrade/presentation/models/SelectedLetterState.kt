package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.business.models.Letter

data class SelectedLetterState(
	val letter: Letter,
	val cost: Int,
	val canAfford: Boolean,
	val onUpgrade: () -> Unit,
	val onDismiss: () -> Unit,
)
