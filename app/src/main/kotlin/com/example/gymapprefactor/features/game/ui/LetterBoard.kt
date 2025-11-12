package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.gymapprefactor.common.components.ui.LetterRouter
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.viewmodel.ScoreAnimationPayload
import kotlinx.coroutines.sync.Mutex
import kotlin.math.roundToInt

@Composable
fun LetterBoard(
	state: GameScreenState.Playing,
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
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
	val gridColumns = 5

	println("PlayedLetters: ${boardState.playedLetters.map { it.id }}")

	InvalidWordShakeHandler(
		invalidWordTrigger = invalidWordTrigger,
		shakeOffset = shakeOffset,
		shakeMutex = shakeMutex,
		onInvalidWordConsumed = onInvalidWordConsumed
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
		Column(modifier = Modifier.fillMaxSize()) {
			LetterBoardTopBar(
				playButton = state.playButton,
				discardButton = state.discardButton,
				bag = state.bag,
				roundsRemainingState = state.roundsRemainingState,
				discardsRemainingState = state.discardsRemainingState,
				playedLetters = boardState.playedLetters
			)

			ScoreLane(scoreState = scoreState)

			PlayedArea(
				boardState = boardState,
				shakeOffset = shakeOffset
			)

			Spacer(modifier = Modifier.height(16.dp))

			HoldingArea(
				boardState = boardState,
				gridColumns = gridColumns
			)
		}

		LetterOverlays(
			boardState = boardState,
			scoreState = scoreState
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
