package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ImageRouter
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.features.game.presentation.models.components.DiscardsRemainingState
import com.example.gymapprefactor.ui.theme.common

@Composable
fun DiscardsRemainingRouter(
	state: DiscardsRemainingState,
	modifier: Modifier = Modifier,
) {
	when (state) {
		is DiscardsRemainingState.Content -> DiscardsRemainingContent(state, modifier)
		is DiscardsRemainingState.None -> Unit
	}
}

@Composable
private fun DiscardsRemainingContent(
	state: DiscardsRemainingState.Content,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.width(150.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		ImageRouter(state.image)
		OutlinedText(
			text = state.remaining.toString(),
			textAlign = TextAlign.Center,
			textStyle = common,
			outlineWidth = 5,
			useGlow = true,
		)
	}
}

@Preview
@Composable
private fun DiscardsRemainingRouterPreview() {
    DiscardsRemainingRouter(
        state = DiscardsRemainingState.Content(
            image = ImageState.DiscardsLeftIcon,
            remaining = 3
        )
    )
}
