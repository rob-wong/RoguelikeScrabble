package com.example.gymapprefactor.features.game.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.features.game.presentation.models.EffectAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState.None
import com.example.gymapprefactor.features.game.presentation.models.GlyphAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.ScoreAnimationPayload
import com.example.gymapprefactor.features.game.presentation.viewmodel.GameViewModelImpl
import kotlinx.coroutines.CoroutineScope
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

	val animationQueues = rememberAnimationQueues()
	val coroutineScope = rememberCoroutineScope()

	setupEventCollectors(viewModel, animationQueues)

	val callbacks = createAnimationCallbacks(
		animationQueues.invalidWordTrigger,
		animationQueues.levelAdvanceShakeTrigger,
		animationQueues.scoreQueue,
		animationQueues.effectAnimationQueue,
		animationQueues.glyphAnimationQueue,
		coroutineScope,
		viewModel
	)
	val activeScoreAnimation = animationQueues.scoreQueue.firstOrNull()
	val activeEffectAnimations = animationQueues.effectAnimationQueue.firstOrNull()
	val activeGlyphAnimation = animationQueues.glyphAnimationQueue.firstOrNull()

	when (val state = screenState) {
		is GameScreenState.Playing ->
			GamePlayScreen(
				state = state,
				invalidWordTrigger = animationQueues.invalidWordTrigger.value,
				onInvalidWordConsumed = callbacks.invalidWordConsumed,
				levelAdvanceShakeTrigger = animationQueues.levelAdvanceShakeTrigger.value,
				onLevelAdvanceShakeConsumed = callbacks.levelAdvanceShakeConsumed,
				scoreBreakdown = activeScoreAnimation,
				onScoreAnimationConsumed = callbacks.scoreAnimationConsumed,
				onScoreAnimationComplete = callbacks.scoreAnimationComplete,
				effectAnimations = activeEffectAnimations,
				onEffectAnimationConsumed = callbacks.effectAnimationConsumed,
				onEffectAnimationComplete = callbacks.effectAnimationComplete,
				glyphAnimation = activeGlyphAnimation,
				onGlyphAnimationComplete = callbacks.glyphAnimationComplete,
				modifier = modifier
			)
		is None -> Unit
	}
}

private data class AnimationCallbacks(
	val invalidWordConsumed: () -> Unit,
	val levelAdvanceShakeConsumed: () -> Unit,
	val scoreAnimationConsumed: () -> Unit,
	val scoreAnimationComplete: () -> Unit,
	val effectAnimationConsumed: () -> Unit,
	val effectAnimationComplete: () -> Unit,
	val glyphAnimationComplete: () -> Unit
)

@Composable
private fun createAnimationCallbacks(
	invalidWordTrigger: MutableState<Boolean>,
	levelAdvanceShakeTrigger: MutableState<Boolean>,
	scoreQueue: MutableList<ScoreAnimationPayload>,
	effectAnimationQueue: MutableList<List<EffectAnimationPayload>>,
	glyphAnimationQueue: MutableList<com.example.gymapprefactor.features.game.presentation.models.GlyphAnimationPayload>,
	coroutineScope: CoroutineScope,
	viewModel: GameViewModelImpl
): AnimationCallbacks {
	return AnimationCallbacks(
		invalidWordConsumed = { invalidWordTrigger.value = false },
		levelAdvanceShakeConsumed = { levelAdvanceShakeTrigger.value = false },
		scoreAnimationConsumed = {
			if (scoreQueue.isNotEmpty()) {
				scoreQueue.removeAt(0)
			}
		},
		scoreAnimationComplete = {
			coroutineScope.launch {
				viewModel.scoreAnimationComplete.emit(Unit)
			}
		},
		effectAnimationConsumed = {
			if (effectAnimationQueue.isNotEmpty()) {
				effectAnimationQueue.removeAt(0)
			}
		},
		effectAnimationComplete = {
			coroutineScope.launch {
				viewModel.effectAnimationComplete.emit(Unit)
			}
		},
		glyphAnimationComplete = {
			if (glyphAnimationQueue.isNotEmpty()) {
				glyphAnimationQueue.removeAt(0)
			}
			coroutineScope.launch {
				viewModel.glyphAnimationComplete.emit(Unit)
			}
		}
	)
}

private data class AnimationQueues(
	val invalidWordTrigger: MutableState<Boolean>,
	val levelAdvanceShakeTrigger: MutableState<Boolean>,
	val scoreQueue: MutableList<ScoreAnimationPayload>,
	val effectAnimationQueue: MutableList<List<EffectAnimationPayload>>,
	val glyphAnimationQueue: MutableList<GlyphAnimationPayload>
)

@Composable
private fun rememberAnimationQueues(): AnimationQueues {
	val invalidWordTrigger = remember { mutableStateOf(false) }
	val levelAdvanceShakeTrigger = remember { mutableStateOf(false) }
	val scoreQueue = remember { mutableStateListOf<ScoreAnimationPayload>() }
	val effectAnimationQueue = remember { mutableStateListOf<List<EffectAnimationPayload>>() }
	val glyphAnimationQueue = remember { mutableStateListOf<GlyphAnimationPayload>() }
	return AnimationQueues(invalidWordTrigger, levelAdvanceShakeTrigger, scoreQueue, effectAnimationQueue, glyphAnimationQueue)
}

@Composable
private fun setupEventCollectors(
	viewModel: GameViewModelImpl,
	animationQueues: AnimationQueues
) {
	LaunchedEffect(Unit) {
		viewModel.invalidWordEvent.collectLatest {
			animationQueues.invalidWordTrigger.value = true
		}
	}

	LaunchedEffect(Unit) {
		viewModel.levelAdvanceShakeTrigger.collectLatest {
			animationQueues.levelAdvanceShakeTrigger.value = true
		}
	}

	LaunchedEffect(Unit) {
		viewModel.scoreEvent.collectLatest { payload ->
			println("GameRoot: received score payload size=${payload.letterScores.size} " +
					"ids=${payload.letterScores.map { it.first }}")
			animationQueues.scoreQueue.add(payload)
		}
	}

	LaunchedEffect(Unit) {
		viewModel.effectAnimationEvent.collectLatest { payload ->
			println("GameRoot: received effect animation payload size=${payload.size}")
			animationQueues.effectAnimationQueue.add(payload)
		}
	}

	LaunchedEffect(Unit) {
		viewModel.glyphAnimationEvent.collectLatest { payload ->
			println("GameRoot: received glyph animation payload amount=${payload.amount}")
			animationQueues.glyphAnimationQueue.add(payload)
		}
	}
}
