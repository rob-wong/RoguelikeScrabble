package com.example.gymapprefactor.features.game.presentation.models.midshop

import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState

data class MidshopLetterSelectionState(
	val needsSelection: Boolean,
	val letters: List<Letter>,
	val selectedLetter: Letter?,
	val confirmButton: ButtonState,
	val onLetterSelected: ((Letter) -> Unit)?,
	val onConfirmed: (() -> Unit)?
)
