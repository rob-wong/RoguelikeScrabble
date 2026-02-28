package com.cypherose.features.game.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.common.components.ui.OutlinedText
import com.cypherose.features.game.presentation.models.components.DiscardsRemainingState
import com.cypherose.ui.theme.common

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
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		ImageRouter(state.image, Modifier.width(70.dp))
		OutlinedText(
			text = state.remaining.toString(),
			textAlign = TextAlign.Center,
			textStyle = common,
			outlineWidth = 5,
			useGlow = false,
		)
	}
}

@Preview
@Composable
private fun DiscardsRemainingRouterPreview() {
    DiscardsRemainingRouter(
        state = DiscardsRemainingState.Content(
            image = ImageState.Basic.DiscardsLeftIcon,
            remaining = 1000
        )
    )
}
