package com.example.gymapprefactor.features.upgrade.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.ui.ResourceBarRouter
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState
import com.example.gymapprefactor.features.upgrade.presentation.viewmodel.UpgradeScreenViewModelImpl

@Composable
fun UpgradeScreen(
	modifier: Modifier = Modifier,
	viewModel: UpgradeScreenViewModelImpl = hiltViewModel()
) {

	val state: UpgradeScreenState by viewModel.state.collectAsStateWithLifecycle(UpgradeScreenState.None)

	when(val screenState = state) {
		is UpgradeScreenState.Content -> UpgradeScreenLayout(
			screenState,
			onAnimationComplete = { viewModel.onAnimationComplete() },
			modifier
		)
		is UpgradeScreenState.None -> Unit
	}
}

@Composable
private fun UpgradeScreenLayout(
	state: UpgradeScreenState.Content,
	onAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(modifier.fillMaxSize()) {
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
		
		state.selectedLetter?.let { selectedLetter ->
			LetterSelectionOverlay(
				state = selectedLetter,
				modifier = Modifier.fillMaxSize()
			)
		}
		
		state.upgradeAnimation?.let { animation ->
			UpgradeAnimationOverlay(
				payload = animation,
				onAnimationComplete = onAnimationComplete,
				modifier = Modifier.fillMaxSize()
			)
		}
	}
}

@Composable
private fun UpgradeScreenContent(
	state: UpgradeScreenState.Content,
	modifier: Modifier = Modifier,
) {
	Column(modifier) {
		LazyVerticalGrid(
			columns = if (DeviceUtil.isLandscape) GridCells.Fixed(8) else GridCells.Fixed(4),
		) {
			itemsIndexed(
				state.letters,
			) { index, item ->
				UpgradeLetterRouter(item, Modifier.padding(bottom = 40.dp))
			}
		}
	}
}

