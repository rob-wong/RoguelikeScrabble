package com.example.gymapprefactor.features.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.ui.ResourceBarRouter
import com.example.gymapprefactor.common.components.ui.ScreenBackgroundRouter
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState

@Composable
fun GamePlayScreen(
	state: GameScreenState.Playing,
	modifier: Modifier = Modifier
) {
	Box(modifier) {
		ScreenBackgroundRouter(ScreenBackgroundState.Game)
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			ResourceBarRouter(state.resourceBar)
			Column(Modifier.fillMaxSize()) { } // placeholder for spacing
		}
	}
}

@DevicePreviews
@Composable
private fun GamePlayScreenPreview() {
	GamePlayScreen(GameScreenState.Playing(
		resourceBar = ResourceBarState.Content(
			runeState = ResourceState.Content(
				amount = "30",
				icon = ImageState.RuneIcon
			),
			glyphState = ResourceState.Content(
				amount = "20",
				icon = ImageState.GlyphIcon
			),
		),
	))
}
