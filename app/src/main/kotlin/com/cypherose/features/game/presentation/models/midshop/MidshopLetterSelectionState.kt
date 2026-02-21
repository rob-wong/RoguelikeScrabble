package com.cypherose.features.game.presentation.models.midshop

import com.cypherose.business.models.Letter
import com.cypherose.common.components.buttons.presentation.ButtonState

data class MidshopLetterSelectionState(
	val needsSelection: Boolean,
	val letters: List<Letter>,
	val selectedLetter: Letter?,
	val confirmButton: ButtonState,
	val onLetterSelected: ((Letter) -> Unit)?,
	val onConfirmed: (() -> Unit)?
)
