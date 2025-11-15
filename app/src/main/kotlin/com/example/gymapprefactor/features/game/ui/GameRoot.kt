package com.example.gymapprefactor.features.game.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState.None
import com.example.gymapprefactor.features.game.presentation.viewmodel.GameViewModelImpl
import com.example.gymapprefactor.features.game.presentation.models.ScoreAnimationPayload
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
	val levelAdvanceShakeTrigger = remember { mutableStateOf(false) }
	val scoreQueue = remember { mutableStateListOf<ScoreAnimationPayload>() }
	val coroutineScope = rememberCoroutineScope()

	LaunchedEffect(Unit) {
		viewModel.invalidWordEvent.collectLatest {
			invalidWordTrigger.value = true
		}
	}

	LaunchedEffect(Unit) {
		viewModel.levelAdvanceShakeTrigger.collectLatest {
			levelAdvanceShakeTrigger.value = true
		}
	}

	LaunchedEffect(Unit) {
		viewModel.scoreEvent.collectLatest { payload ->
			println("GameRoot: received score payload size=${payload.letterScores.size} " +
					"ids=${payload.letterScores.map { it.first }}")
			scoreQueue.add(payload)
		}
	}

	val invalidWordConsumed: () -> Unit = { invalidWordTrigger.value = false }
	val levelAdvanceShakeConsumed: () -> Unit = { levelAdvanceShakeTrigger.value = false }
	val scoreAnimationConsumed: () -> Unit = {
		if (scoreQueue.isNotEmpty()) {
			scoreQueue.removeAt(0)
		}
	}
	val scoreAnimationComplete: () -> Unit = {
		coroutineScope.launch {
			viewModel.scoreAnimationComplete.emit(Unit)
		}
	}
	val activeScoreAnimation = scoreQueue.firstOrNull()
	println("GameRoot: activeScoreAnimation size=${activeScoreAnimation?.letterScores?.size}")

	when (val state = screenState) {
		is GameScreenState.Playing ->
			GamePlayScreen(
				state = state,
				invalidWordTrigger = invalidWordTrigger.value,
				onInvalidWordConsumed = invalidWordConsumed,
				levelAdvanceShakeTrigger = levelAdvanceShakeTrigger.value,
				onLevelAdvanceShakeConsumed = levelAdvanceShakeConsumed,
				scoreBreakdown = activeScoreAnimation,
				onScoreAnimationConsumed = scoreAnimationConsumed,
				onScoreAnimationComplete = scoreAnimationComplete,
				modifier = modifier
			)
		is None -> Unit
	}
}
