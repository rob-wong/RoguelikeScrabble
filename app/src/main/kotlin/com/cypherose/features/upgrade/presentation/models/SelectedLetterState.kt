package com.cypherose.features.upgrade.presentation.models

import com.cypherose.business.models.Letter

data class SelectedLetterState(
	val letter: Letter,
	val cost: Int,
	val canAfford: Boolean,
	val onUpgrade: () -> Unit,
	val onDismiss: () -> Unit,
)
