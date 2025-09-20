package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymapprefactor.R
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ImageState.*

@Composable
fun ImageRouter(
	state: ImageState,
	modifier: Modifier = Modifier
) {
	val painter = when (state) {
		is SettingsButton -> painterResource(R.drawable.settings_gear)
	}

	Image(
		painter = painter,
		contentDescription = null,
		modifier = modifier.background(color = Color.Transparent),
		alignment = Alignment.Center,
		contentScale = ContentScale.Crop,
	)
}

@Composable
@Preview
private fun ImageRouterPreview() {
	ImageRouter(SettingsButton)
}
