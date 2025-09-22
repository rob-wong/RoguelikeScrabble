package com.example.gymapprefactor.features.shop.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.ui.ResourceBarRouter
import com.example.gymapprefactor.common.components.ui.ScreenBackgroundRouter
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenState
import com.example.gymapprefactor.features.shop.presentation.models.ShopScreenState.None
import com.example.gymapprefactor.features.shop.presentation.viewmodel.ShopScreenViewModelImpl

@Composable
fun ShopScreen(
	modifier: Modifier = Modifier,
	viewModel: ShopScreenViewModelImpl = hiltViewModel()
) {
	val state: ShopScreenState by viewModel.state.collectAsStateWithLifecycle(None)

	when(val screenState = state) {
		is ShopScreenState.Content -> ShopScreenLayout(screenState, modifier)
		is None -> Unit
	}
}

@Composable
private fun ShopScreenLayout(
	state: ShopScreenState.Content,
	modifier: Modifier = Modifier,
) {
	Box(modifier) {
		ScreenBackgroundRouter(ScreenBackgroundState.Shop)

		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			ResourceBarRouter(
				state.resourceBar
			)
			ShopScreenContent(state, Modifier.fillMaxSize())
		}
	}
}

@Composable
private fun ShopScreenContent(
	state: ShopScreenState.Content,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier
	) {
		state
	}
}

@Composable
@DevicePreviews
private fun ShopScreenPreview() {
	ShopScreenLayout(
		state = ShopScreenState.Content(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceState.Content(
					amount = "30",
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceState.None
			),
		)
	)
}
