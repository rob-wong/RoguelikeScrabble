package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.presentation.BagState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.ui.theme.common

@Composable
fun BagRouter(
	state: BagState,
	modifier: Modifier = Modifier,
) {
	when (state) {
		is BagState.Content -> BagContent(state, modifier)
		else -> Unit
	}
}

@Composable
private fun BagContent(
	state: BagState.Content,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		ImageRouter(state.image, Modifier.width(100.dp))
		OutlinedText(
			text = textRouter(state),
			textAlign = TextAlign.Center,
			textStyle = common,
			outlineWidth = 5,
			useGlow = false,
		)
	}
}

@ReadOnlyComposable
private fun textRouter(state: BagState.Content): String {
	return state.currentLetters.toString() + " / " + state.maxLetters.toString()
}

@Composable
@Preview
private fun BagPreview() {
	BagRouter(
		BagState.Content(
			image = ImageState.BasicBagIcon,
			currentLetters = 5,
			maxLetters = 10
		)
	)
}
