package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
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
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.ScoreAnimationPayload
import com.example.gymapprefactor.features.game.ui.components.DiscardsRemainingRouter
import com.example.gymapprefactor.features.game.ui.components.EffectsColumn
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
	modifier: Modifier = Modifier
) {
	val tileWidthPx = with(LocalDensity.current) { 48.dp.toPx() }
	val boardState = rememberLetterBoardState(state.letters, tileWidthPx)
	val scoreState = rememberScoreAnimationState()
	val shakeOffset = remember { Animatable(0f) }
	val shakeMutex = remember { Mutex() }
	val holdingAreaShakeOffset = remember { Animatable(0f) }
	val bagShakeOffset = remember { Animatable(0f) }
	val levelAdvanceShakeMutex = remember { Mutex() }
	val gridColumns = 5

	println("PlayedLetters: ${boardState.playedLetters.map { it.id }}")

	LetterBoardShakeHandlers(
		invalidWordTrigger = invalidWordTrigger,
		onInvalidWordConsumed = onInvalidWordConsumed,
		levelAdvanceShakeTrigger = levelAdvanceShakeTrigger,
		onLevelAdvanceShakeConsumed = onLevelAdvanceShakeConsumed,
		shakeOffset = shakeOffset,
		shakeMutex = shakeMutex,
		holdingAreaShakeOffset = holdingAreaShakeOffset,
		bagShakeOffset = bagShakeOffset,
		levelAdvanceShakeMutex = levelAdvanceShakeMutex
	)

	ScoreAnimationHandler(
		scoreBreakdown = scoreBreakdown,
		scoreState = scoreState,
		boardState = boardState,
		onScoreAnimationConsumed = onScoreAnimationConsumed,
		onScoreAnimationComplete = onScoreAnimationComplete
	)

	Box(
		modifier = modifier
			.fillMaxSize()
			.onGloballyPositioned { coords ->
				boardState.rootOffset = coords.boundsInWindow().topLeft
			}
	) {
		LetterBoardContent(
			state = state,
			boardState = boardState,
			scoreState = scoreState,
			shakeOffset = shakeOffset,
			holdingAreaShakeOffset = holdingAreaShakeOffset,
			gridColumns = gridColumns
		)

		// Bag icon on the right side below holding area
		BagRouter(
			state.bag,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(bottom = 60.dp, end = 8.dp)
				.wrapContentWidth()
				.offset { IntOffset(bagShakeOffset.value.roundToInt(), 0) }
		)

		LetterOverlays(
			boardState = boardState,
			scoreState = scoreState
		)
	}
}

@Composable
private fun LetterBoardShakeHandlers(
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	levelAdvanceShakeTrigger: Boolean,
	onLevelAdvanceShakeConsumed: () -> Unit,
	shakeOffset: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
	shakeMutex: Mutex,
	holdingAreaShakeOffset: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
	bagShakeOffset: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
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
	shakeOffset: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
	holdingAreaShakeOffset: Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
	gridColumns: Int
) {
	Box(modifier = Modifier.fillMaxSize()) {
		Column(modifier = Modifier.fillMaxSize()) {
			// Rounds and discards remaining on the right side above scoring lane
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

		ScoreLane(scoreState = scoreState)

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
			effects = state.effects,
			modifier = Modifier
				.align(Alignment.TopStart)
				.padding(top = 8.dp, start = 8.dp)
		)
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
