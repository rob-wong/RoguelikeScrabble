package com.example.gymapprefactor.common.components.ui

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
		is ScreenBackgroundState.Home -> ScreenBackgroundContent(
			painter = painterResource(id = R.drawable.background),
			modifier = modifier
		)
		is ScreenBackgroundState.None -> Unit
	}
}
@Composable
fun ScreenBackgroundContent(
	painter: Painter,
	modifier: Modifier = Modifier
) {
	androidx.compose.foundation.Image(
		painter = painter,
		contentDescription = null,
		modifier = modifier.fillMaxSize(),
		contentScale = ContentScale.Crop,
		alignment = Alignment.BottomCenter,
	)
}

@DevicePreviews
@Composable
fun ScreenBackgroundRouterPreview() {
	ScreenBackgroundRouter(state = ScreenBackgroundState.Home)
}
