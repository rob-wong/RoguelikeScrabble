package com.cypherose.common.components.presentation

sealed class LetterState {
	data class Display(
		val type: DeckType,
		val letter: Char,
		val level: Int,
	) : LetterState()

	data object None : LetterState()
}
