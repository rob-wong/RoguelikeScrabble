package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.ui.BagRouter
import com.example.gymapprefactor.common.components.ui.LetterRouter
import com.example.gymapprefactor.features.game.presentation.models.animation.EffectAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.animation.ScoreAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.components.EnemyHealthBarState
import com.example.gymapprefactor.features.game.presentation.models.components.RoundsRemainingState
import com.example.gymapprefactor.features.game.ui.animation.AnimationHandler
import com.example.gymapprefactor.features.game.ui.animation.EffectAnimationState
import com.example.gymapprefactor.features.game.ui.animation.InvalidWordShakeHandler
import com.example.gymapprefactor.features.game.ui.animation.LevelAdvanceShakeHandler
import com.example.gymapprefactor.features.game.ui.animation.ScoreAnimationState
import com.example.gymapprefactor.features.game.ui.animation.ScoreFadeOutOnDamageHandler
import com.example.gymapprefactor.features.game.ui.animation.rememberEffectAnimationState
import com.example.gymapprefactor.features.game.ui.animation.rememberScoreAnimationState
import com.example.gymapprefactor.features.game.ui.components.DiscardsRemainingRouter
import com.example.gymapprefactor.features.game.ui.components.EffectsColumn
import com.example.gymapprefactor.features.game.ui.components.InputButtonRouter
import com.example.gymapprefactor.features.game.ui.components.RoundsRemainingRouter
import kotlinx.coroutines.sync.Mutex
import kotlin.math.roundToInt

@Composable
fun LetterBoard(
	state: GameScreenState.Playing,
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	levelAdvanceShakeTrigger: Boolean,
	onLevelAdvanceShakeConsumed: () -> Unit,
	scoreBreakdown: ScoreAnimationPayload?,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit,
	effectAnimations: List<EffectAnimationPayload>?,
	onEffectAnimationConsumed: () -> Unit,
	onEffectAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier
) {
	val letterBoardData = rememberLetterBoardData(state.letters)
	
	// Clear animation state when starting a new game or advancing to a new level
	val currentRound = when (val roundsState = state.roundsRemainingState) {
		is RoundsRemainingState.Content -> roundsState.currentRound
		else -> null
	}
	// Track previous round to detect transitions to round 1 (new level)
	val previousRound = remember { mutableStateOf<Int?>(null) }
	LaunchedEffect(currentRound, state.currentRoundEffects.size) {
		// Clear when:
		// 1. Transitioning to round 1 from a higher round (new level started)
		// 2. Effects list is empty at round 1 (new game started)
		val isNewLevel = currentRound == 1 && previousRound.value != null && previousRound.value != 1
		val isNewGame = state.currentRoundEffects.isEmpty() && currentRound == 1
		if (isNewLevel || isNewGame) {
			letterBoardData.effectState.clear()
			letterBoardData.scoreState.clear()
		}
		previousRound.value = currentRound
	}
	
	setupLetterBoardAnimations(
		invalidWordTrigger,
		onInvalidWordConsumed,
		levelAdvanceShakeTrigger,
		onLevelAdvanceShakeConsumed,
		letterBoardData
	)

	// Track which effect is currently animating for scrolling
	val animatingEffectId = remember { mutableStateOf<String?>(null) }

	AnimationHandler(
		scoreBreakdown = scoreBreakdown,
		effectAnimations = effectAnimations,
		scoreState = letterBoardData.scoreState,
		effectState = letterBoardData.effectState,
		boardState = letterBoardData.boardState,
		onScoreAnimationConsumed = onScoreAnimationConsumed,
		onScoreAnimationComplete = onScoreAnimationComplete,
		onEffectAnimationConsumed = onEffectAnimationConsumed,
		onEffectAnimationComplete = onEffectAnimationComplete,
		onEffectAnimationStart = { effectId -> animatingEffectId.value = effectId }
	)

	// Watch for health changes and fade out score when damage is applied
	val currentHealth = when (val healthState = state.enemyHealthBarState) {
		is EnemyHealthBarState.Content -> healthState.currentHealth
		else -> null
	}
	ScoreFadeOutOnDamageHandler(
		currentHealth = currentHealth,
		scoreState = letterBoardData.scoreState,
		effectState = letterBoardData.effectState
	)

	LetterBoardContentBox(
		state = state,
		letterBoardData = letterBoardData,
		animatingEffectId = animatingEffectId.value,
		modifier = modifier
	)
}

private data class LetterBoardData(
	val boardState: LetterBoardState,
	val scoreState: ScoreAnimationState,
	val effectState: EffectAnimationState,
	val shakeOffset: Animatable<Float, AnimationVector1D>,
	val holdingAreaShakeOffset: Animatable<Float, AnimationVector1D>,
	val bagShakeOffset: Animatable<Float, AnimationVector1D>,
	val shakeMutex: Mutex,
	val levelAdvanceShakeMutex: Mutex,
	val gridColumns: Int
)

@Composable
private fun rememberLetterBoardData(letters: List<GameScreenState.DraggableLetter>): LetterBoardData {
	val tileWidthPx = with(LocalDensity.current) { 48.dp.toPx() }
	val boardState = rememberLetterBoardState(letters, tileWidthPx)
	val scoreState = rememberScoreAnimationState()
	val effectState = rememberEffectAnimationState()
	val shakeOffset = remember { Animatable(0f) }
	val holdingAreaShakeOffset = remember { Animatable(0f) }
	val bagShakeOffset = remember { Animatable(0f) }
	val shakeMutex = remember { Mutex() }
	val levelAdvanceShakeMutex = remember { Mutex() }
	return LetterBoardData(
		boardState = boardState,
		scoreState = scoreState,
		effectState = effectState,
		shakeOffset = shakeOffset,
		holdingAreaShakeOffset = holdingAreaShakeOffset,
		bagShakeOffset = bagShakeOffset,
		shakeMutex = shakeMutex,
		levelAdvanceShakeMutex = levelAdvanceShakeMutex,
		gridColumns = 5
	)
}

@Composable
private fun setupLetterBoardAnimations(
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	levelAdvanceShakeTrigger: Boolean,
	onLevelAdvanceShakeConsumed: () -> Unit,
	letterBoardData: LetterBoardData
) {
	LetterBoardShakeHandlers(
		invalidWordTrigger = invalidWordTrigger,
		onInvalidWordConsumed = onInvalidWordConsumed,
		levelAdvanceShakeTrigger = levelAdvanceShakeTrigger,
		onLevelAdvanceShakeConsumed = onLevelAdvanceShakeConsumed,
		shakeOffset = letterBoardData.shakeOffset,
		shakeMutex = letterBoardData.shakeMutex,
		holdingAreaShakeOffset = letterBoardData.holdingAreaShakeOffset,
		bagShakeOffset = letterBoardData.bagShakeOffset,
		levelAdvanceShakeMutex = letterBoardData.levelAdvanceShakeMutex
	)

	LevelAdvanceShakeHandler(
		levelAdvanceShakeTrigger = levelAdvanceShakeTrigger,
		holdingAreaShakeOffset = letterBoardData.holdingAreaShakeOffset,
		bagShakeOffset = letterBoardData.bagShakeOffset,
		shakeMutex = letterBoardData.levelAdvanceShakeMutex,
		onLevelAdvanceShakeConsumed = onLevelAdvanceShakeConsumed
	)
}

@Composable
private fun LetterBoardContentBox(
	state: GameScreenState.Playing,
	letterBoardData: LetterBoardData,
	animatingEffectId: String?,
	modifier: Modifier
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.onGloballyPositioned { coords ->
				letterBoardData.boardState.rootOffset = coords.boundsInWindow().topLeft
			}
	) {
		LetterBoardContent(
			state = state,
			boardState = letterBoardData.boardState,
			scoreState = letterBoardData.scoreState,
			effectState = letterBoardData.effectState,
			shakeOffset = letterBoardData.shakeOffset,
			holdingAreaShakeOffset = letterBoardData.holdingAreaShakeOffset,
			gridColumns = letterBoardData.gridColumns,
			animatingEffectId = animatingEffectId
		)

		BagRouter(
			state.bag,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(bottom = 60.dp, end = 8.dp)
				.wrapContentWidth()
				.offset { IntOffset(letterBoardData.bagShakeOffset.value.roundToInt(), 0) }
		)

		LetterOverlays(
			boardState = letterBoardData.boardState,
			scoreState = letterBoardData.scoreState
		)
	}
}

@Composable
private fun LetterBoardShakeHandlers(
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	levelAdvanceShakeTrigger: Boolean,
	onLevelAdvanceShakeConsumed: () -> Unit,
	shakeOffset: Animatable<Float, AnimationVector1D>,
	shakeMutex: Mutex,
	holdingAreaShakeOffset: Animatable<Float, AnimationVector1D>,
	bagShakeOffset: Animatable<Float, AnimationVector1D>,
	levelAdvanceShakeMutex: Mutex
) {
	InvalidWordShakeHandler(
		invalidWordTrigger = invalidWordTrigger,
		shakeOffset = shakeOffset,
		shakeMutex = shakeMutex,
		onInvalidWordConsumed = onInvalidWordConsumed
	)

	LevelAdvanceShakeHandler(
		levelAdvanceShakeTrigger = levelAdvanceShakeTrigger,
		holdingAreaShakeOffset = holdingAreaShakeOffset,
		bagShakeOffset = bagShakeOffset,
		shakeMutex = levelAdvanceShakeMutex,
		onLevelAdvanceShakeConsumed = onLevelAdvanceShakeConsumed
	)
}

@Composable
private fun LetterBoardContent(
	state: GameScreenState.Playing,
	boardState: LetterBoardState,
	scoreState: ScoreAnimationState,
	effectState: EffectAnimationState,
	shakeOffset: Animatable<Float, AnimationVector1D>,
	holdingAreaShakeOffset: Animatable<Float, AnimationVector1D>,
	gridColumns: Int,
	animatingEffectId: String?
) {
	Box(modifier = Modifier.fillMaxSize()) {
		Column(modifier = Modifier.fillMaxSize()) {
			RoundsAndDiscards(state)

		ScoreLane(scoreState = scoreState, effectState = effectState)

		PlayedArea(
			boardState = boardState,
			shakeOffset = shakeOffset
		)

		Spacer(modifier = Modifier.height(16.dp))

		HoldingArea(
			boardState = boardState,
			gridColumns = gridColumns,
			shakeOffset = holdingAreaShakeOffset
		)

		// Confirm and discard buttons centered below holding area
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp),
			horizontalArrangement = Arrangement.Center
		) {
			InputButtonRouter(
				state.playButton,
				boardState.playedLetters,
				Modifier.size(80.dp, 40.dp)
			)
			Spacer(modifier = Modifier.width(8.dp))
			ButtonRouter(
				state.discardButton,
				Modifier.size(80.dp, 40.dp)
			)
		}
		}
		
		// Effects column on the left side
		EffectsColumn(
			activeGameEffects = state.activeGameEffects,
			currentRoundEffects = state.currentRoundEffects,
			effectState = effectState,
			effectDescriptors = state.effectDescriptors,
			animatingEffectId = animatingEffectId,
			modifier = Modifier
				.align(Alignment.TopStart)
				.padding(top = 8.dp, start = 8.dp)
		)
	}
}

@Composable
private fun RoundsAndDiscards(state: GameScreenState.Playing) {
	Box(modifier = Modifier.fillMaxWidth()) {
		Column(
			modifier = Modifier
				.align(Alignment.TopEnd)
				.padding(top = 8.dp, end = 8.dp)
		) {
			RoundsRemainingRouter(
				state = state.roundsRemainingState,
				modifier = Modifier.wrapContentWidth()
			)
			Spacer(modifier = Modifier.height(4.dp))
			DiscardsRemainingRouter(
				state = state.discardsRemainingState,
				modifier = Modifier.wrapContentWidth()
			)
		}
	}
}

internal enum class Area { Holding, Played }

internal fun insertLetterByPosition(
	letter: GameScreenState.DraggableLetter,
	targetList: SnapshotStateList<GameScreenState.DraggableLetter>,
	referenceList: List<GameScreenState.DraggableLetter>,
	dropOffset: Offset,
	slotPositions: Map<String, Offset>
) {
	val sorted = referenceList
		.mapIndexed { index, ref ->
			val slot = slotPositions[ref.id]
			val distance = slot?.let { (it - dropOffset).getDistance() } ?: Float.MAX_VALUE
			index to distance
		}
		.sortedBy { it.second }

	val insertIndex = sorted.firstOrNull()?.first ?: targetList.size
	targetList.remove(letter)
	if (!targetList.contains(letter)) {
		targetList.add(insertIndex.coerceIn(0, targetList.size), letter)
	}
}

@Composable
internal fun LetterItem(
	letter: GameScreenState.DraggableLetter,
	targetOffset: Offset,
	onClick: () -> Unit,
	onDrag: (Offset) -> Unit,
	onDrop: (GameScreenState.DraggableLetter, Offset) -> Unit,
	onDragStateChanged: (String?) -> Unit
) {
	var dragOffset by remember { mutableStateOf(Offset.Zero) }
	var isDragging by remember { mutableStateOf(false) }

	Box(
		modifier = Modifier
			.offset {
				IntOffset(
					(targetOffset.x + dragOffset.x).roundToInt(),
					(targetOffset.y + dragOffset.y).roundToInt()
				)
			}
			.zIndex(if (isDragging) 1f else 0f)
			.pointerInput(letter.id) {
				detectTapGestures {
					onClick()
				}
			}
			.pointerInput(letter.id) {
				detectDragGestures(
					onDragStart = {
						isDragging = true
						onDragStateChanged(letter.id)
					},
					onDragEnd = {
						isDragging = false
						onDragStateChanged(null)
						onDrop(letter, targetOffset + dragOffset)
						dragOffset = Offset.Zero
					},
					onDragCancel = {
						isDragging = false
						onDragStateChanged(null)
						dragOffset = Offset.Zero
					},
					onDrag = { change, dragAmount ->
						change.consume()
						dragOffset += dragAmount
						onDrag(dragOffset)
					}
				)
			}
	) {
		LetterRouter(letter.letterState)
	}
}
