package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ImageRouter
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.features.game.presentation.models.components.RoundsRemainingState
import com.example.gymapprefactor.ui.theme.common

@Composable
fun RoundsRemainingRouter(
	state: RoundsRemainingState,
	modifier: Modifier = Modifier,
) {
	when (state) {
		is RoundsRemainingState.Content -> RoundsRemainingContent(state, modifier)
		is RoundsRemainingState.None -> Unit
	}
}

@Composable
private fun RoundsRemainingContent(
	state: RoundsRemainingState.Content,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier.width(150.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		ImageRouter(state.image)
		OutlinedText(
			text = textRouter(state),
			textAlign = TextAlign.Center,
			textStyle = common,
			outlineWidth = 5,
			useGlow = true,
		)
	}
}

@ReadOnlyComposable
private fun textRouter(state: RoundsRemainingState.Content): String {
	return state.currentRound.toString() + " / " + state.maxRounds.toString()
}

@Preview
@Composable
private fun RoundsRemainingRouterPreview() {
    RoundsRemainingRouter(
        state = RoundsRemainingState.Content(
            image = ImageState.PlaysLeftIcon,
            currentRound = 5,
            maxRounds = 10
        )
    )
}
