package com.example.gymapprefactor.common.components.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState

class LetterProvider : PreviewParameterProvider<LetterState> {
	override val values = sequenceOf(
		LetterState.Display(
			type = DeckType.Default,
			letter = 'A',
			level = 1
		),
		LetterState.Display(
			type = DeckType.Default,
			letter = 'A',
			level = 2
		),
		LetterState.Display(
			type = DeckType.Default,
			letter = 'A',
			level = 3
		),
		LetterState.Display(
			type = DeckType.Default,
			letter = 'A',
			level = 4
		),
		LetterState.Display(
			type = DeckType.Default,
			letter = 'A',
			level = 5
		),
	)
}