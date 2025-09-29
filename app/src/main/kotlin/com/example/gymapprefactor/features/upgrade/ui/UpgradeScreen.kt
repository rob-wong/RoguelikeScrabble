package com.example.gymapprefactor.features.upgrade.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.ui.ResourceBarRouter
import com.example.gymapprefactor.common.components.ui.ScreenBackgroundRouter
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState
import com.example.gymapprefactor.features.upgrade.presentation.viewmodel.UpgradeScreenViewModelImpl

@Composable
fun UpgradeScreen(
	modifier: Modifier = Modifier,
	viewModel: UpgradeScreenViewModelImpl = hiltViewModel()
) {

	val state: UpgradeScreenState by viewModel.state.collectAsStateWithLifecycle(UpgradeScreenState.None)

	when(val screenState = state) {
		is UpgradeScreenState.Content -> UpgradeScreenLayout(screenState, modifier)
		is UpgradeScreenState.None -> Unit
	}
}

@Composable
private fun UpgradeScreenLayout(
	state: UpgradeScreenState.Content,
	modifier: Modifier = Modifier,
) {
	Box(modifier) {
		ScreenBackgroundRouter(ScreenBackgroundState.Upgrade)
		ButtonRouter(state.backButton, Modifier
			.padding(top = 10.dp, end = 10.dp)
			.align(Alignment.TopEnd)
			.size(40.dp)
		)
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			ResourceBarRouter(
				state.resourceBar
			)
			UpgradeScreenContent(state, Modifier.fillMaxSize())
		}
	}
}

@Composable
private fun UpgradeScreenContent(
	state: UpgradeScreenState.Content,
	modifier: Modifier = Modifier,
) {
	Column(modifier) {
		state
	}
}

@Composable
@DevicePreviews
private fun UpgradeScreenPreview() {
	UpgradeScreenLayout(
		state = UpgradeScreenState.Content(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceState.Content(
					amount = "10",
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceState.None
			),
			backButton = IconButtonState.Content(
				onClick = { },
				image = ImageState.BackIcon
			)
		)
	)
}
