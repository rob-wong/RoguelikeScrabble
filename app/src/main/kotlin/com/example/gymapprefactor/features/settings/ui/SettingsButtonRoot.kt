package com.example.gymapprefactor.features.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ButtonRouter
import com.example.gymapprefactor.features.settings.presentation.SettingsRootState
import com.example.gymapprefactor.features.settings.presentation.SettingsRootViewModelImpl

@Composable
fun SettingsRoot(
	modifier: Modifier = Modifier,
	viewModel: SettingsRootViewModelImpl = hiltViewModel()
) {
	val state: SettingsRootState by viewModel.state.collectAsState(SettingsRootState.None)

	when (val screenState = state) {
		is SettingsRootState.Content -> SettingsRootContent(screenState, modifier)
		is SettingsRootState.None -> Unit
	}
}

@Composable
private fun SettingsRootContent(
	state: SettingsRootState.Content,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.TopStart
	) {
		ButtonRouter(
			state.settingsButton
		)
	}
}

@Composable
@DevicePreviews
private fun SettingsRootPreview() {
	SettingsRootContent(
		state = SettingsRootState.Content(
			settingsButton = IconButtonState.Content(
				onClick = { },
				image = ImageState.SettingsButton
			)
		)
	)
}
