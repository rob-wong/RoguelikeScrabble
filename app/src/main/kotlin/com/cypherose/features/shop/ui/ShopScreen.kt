package com.cypherose.features.shop.ui

import androidx.compose.foundation.layout.Arrangement
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
import com.cypherose.app.util.DevicePreviews
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.common.components.buttons.ui.ButtonRouter
import com.cypherose.common.components.ui.ComingSoonOverlay
import com.cypherose.common.components.ui.ResourceBarRouter
import com.cypherose.features.shop.presentation.models.ShopScreenState
import com.cypherose.features.shop.presentation.models.ShopScreenState.None
import com.cypherose.features.shop.presentation.viewmodel.ShopScreenViewModelImpl
import com.cypherose.features.templateengine.ui.ContentRouter

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
	Box(modifier.fillMaxSize()) {
		ButtonRouter(state.backButton, Modifier
			.padding(top = 10.dp, end = 10.dp)
			.align(Alignment.TopEnd)
			.size(40.dp)
		)
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			ResourceBarRouter(
				state.resourceBar
			)
			ShopScreenContent(state, Modifier.fillMaxSize())
		}
		// Temp
		ComingSoonOverlay(
			modifier = Modifier.align(Alignment.Center)
		)
	}
}

@Composable
private fun ShopScreenContent(
	state: ShopScreenState.Content,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically)
	) {
		state.templateStates.forEach { templateState ->
			ContentRouter(templateState)
		}
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
					icon = ImageState.Basic.RuneIcon
				),
				glyphState = ResourceState.None
			),
			backButton = IconButtonState.Content(
				onClick = { },
				image = ImageState.Basic.BackIcon
			),
			templateStates = emptyList()
		)
	)
}
