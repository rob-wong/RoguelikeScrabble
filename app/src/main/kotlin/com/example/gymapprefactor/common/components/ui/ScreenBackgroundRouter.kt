package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.gymapprefactor.R
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState

@Composable
fun ScreenBackgroundRouter(
	state: ScreenBackgroundState,
	modifier: Modifier = Modifier
) {
	when(state) {
		is ScreenBackgroundState.Shop -> ScreenBackgroundContent(
			painter = painterResource(R.drawable.background_shop),
			modifier = modifier,
			alignment = Alignment.Center,
			contentScale = ContentScale.Crop
		)
		is ScreenBackgroundState.Upgrade -> ScreenBackgroundContent(
			painter = painterResource(R.drawable.background_upgrade),
			modifier = modifier,
			alignment = Alignment.Center,
			contentScale = ContentScale.Crop
		)
		is ScreenBackgroundState.Home -> ScreenBackgroundContent(
			painter = painterResource(id = R.drawable.background_home),
			modifier = modifier
		)
		is ScreenBackgroundState.Game -> ScreenBackgroundContent(
			painter = painterResource(id = R.drawable.background_game),
			modifier = modifier
		)
		is ScreenBackgroundState.None -> Unit
	}
}
@Composable
private fun ScreenBackgroundContent(
	painter: Painter,
	modifier: Modifier = Modifier,
	alignment: Alignment = Alignment.BottomCenter,
	contentScale: ContentScale = ContentScale.Crop
) {
	Image(
		painter = painter,
		contentDescription = null,
		modifier = modifier.fillMaxSize(),
		contentScale = contentScale,
		alignment = alignment,
	)
}

@DevicePreviews
@Composable
fun ScreenBackgroundRouterPreview() {
	ScreenBackgroundRouter(state = ScreenBackgroundState.Home)
}
