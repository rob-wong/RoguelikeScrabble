package com.example.gymapprefactor.common.components.buttons.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState.SettingsButton
import com.example.gymapprefactor.common.components.ui.ImageRouter


@Composable
fun IconButtonRouter(
	state: IconButtonState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is IconButtonState.Content -> IconButton(state, modifier)
		is IconButtonState.None -> Unit
	}
}

@Composable
private fun IconButton(
	state: IconButtonState.Content,
	modifier: Modifier = Modifier
) {
	Surface (
		color = Color.Transparent,
		onClick = state.onClick,
		modifier = modifier
			.size(50.dp),
	) {
		ImageRouter(state.image)
	}
}

@Composable
@DevicePreviews
private fun IconButtonPreview() {
	IconButtonRouter(IconButtonState.Content(onClick = { }, image = SettingsButton))
}
