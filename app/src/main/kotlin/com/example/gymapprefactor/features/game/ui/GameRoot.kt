package com.example.gymapprefactor.features.game.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState.None
import com.example.gymapprefactor.features.game.presentation.viewmodel.GameViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GameRoot(
	modifier: Modifier = Modifier,
	key: String = remember { "Game_${System.currentTimeMillis()}" },
) {
	val viewModel: GameViewModelImpl = hiltViewModel(key = key)

	val screenState: GameScreenState by viewModel.state.collectAsStateWithLifecycle(
		None
	)

	val invalidWordTrigger = remember { mutableStateOf(false) }
	LaunchedEffect(Unit) {
		viewModel.invalidWordEvent.collectLatest {
			invalidWordTrigger.value = true
		}
	}

	val invalidWordConsumed: () -> Unit = { invalidWordTrigger.value = false }

	when (val state = screenState) {
		is GameScreenState.Playing ->
			GamePlayScreen(
				state = state,
				invalidWordTrigger = invalidWordTrigger.value,
				onInvalidWordConsumed = invalidWordConsumed,
				modifier = modifier
			)
		is None -> Unit
	}
}
