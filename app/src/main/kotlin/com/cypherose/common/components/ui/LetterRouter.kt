package com.cypherose.common.components.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.presentation.DeckType
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.LetterState
import com.cypherose.common.components.ui.previews.LetterProvider

@Composable
fun LetterRouter(
	state: LetterState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is LetterState.Display -> DisplayLetter(state, modifier)
		is LetterState.None -> Unit
	}
}

@Composable
private fun DisplayLetter(
	state: LetterState.Display,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		LetterBackgroundRouter(state.type, Modifier.size(45.dp))
		LetterContentRouter(state.letter, state.level)
	}
}

@Composable
private fun LetterBackgroundRouter(
	type: DeckType,
	modifier: Modifier = Modifier,
) {
	when (type) {
		DeckType.Default -> ImageRouter(ImageState.Basic.DefaultLetterBackground, modifier)
	}
}

@Composable
private fun LetterContentRouter(
	letter: Char,
	level: Int,
	modifier: Modifier = Modifier
) {
	OutlinedText(
		text = letter.toString(),
		textAlign = TextAlign.Center,
		textStyle = letterFontRouter(level),
		modifier = modifier,
		outlineWidth = 7,
		useGlow = true,
	)
}

@Preview
@Composable
private fun LetterPreview(
	@PreviewParameter(LetterProvider::class) state: LetterState
) {
	LetterRouter(state)
}
