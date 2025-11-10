package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
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
import com.example.gymapprefactor.features.game.ui.components.DiscardsRemainingRouter
import com.example.gymapprefactor.features.game.ui.components.RoundsRemainingRouter
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Thread.sleep
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
@SuppressWarnings("LongMethod","CyclomaticComplexMethod","SpreadOperator")
fun LetterBoard(
	state: GameScreenState.Playing,
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	modifier: Modifier = Modifier
) {
	val tileWidthPx = with(LocalDensity.current) { 48.dp.toPx() }
	val holdingLetters = remember { mutableStateListOf(*state.letters.toTypedArray()) }
	val playedLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }

	val slotPositions = remember { mutableStateMapOf<String, Offset>() }
	val letterAreaMap = remember { mutableStateMapOf<String, Area>() }

	var placeholderIndex by remember { mutableStateOf<Int?>(null) }

	var playedBounds by remember { mutableStateOf<Rect?>(null) }
	var holdingBounds by remember { mutableStateOf<Rect?>(null) }
	var rootOffset by remember { mutableStateOf(Offset.Zero) }
	var draggingLetterId by remember { mutableStateOf<String?>(null) }

	val rowBoundsMap = remember { mutableStateMapOf<Int, Rect>() }
	val gridColumns = 5

	println("PlayedLetters: ${playedLetters.map { it.id }}")

	val shakeOffset = remember { Animatable(0f) }
	val shakeMutex = remember { Mutex() }

	LaunchedEffect(invalidWordTrigger) {
		shakeMutex.withLock {
			if (invalidWordTrigger) {
				shakeOffset.snapTo(20f)
				shakeOffset.animateTo(
					targetValue = 0f,
					animationSpec = repeatable(
						iterations = 4,
						animation = tween(durationMillis = 50),
						repeatMode = RepeatMode.Reverse
					)
				)
			}
		}
		onInvalidWordConsumed()
	}

	Box(
		modifier = modifier
//			.clickable(
//				onClick = { println("shakeOffset: ${shakeOffset.value}, invalidWordTrigger: $invalidWordTrigger") }
//			)
			.fillMaxSize()
			.onGloballyPositioned { coords ->
				rootOffset = coords.boundsInWindow().topLeft
			}
	) {
		Column(modifier = Modifier.fillMaxSize()) {
			Column {
				Row {
					InputButtonRouter(state.playButton, playedLetters, Modifier)
					ButtonRouter(state.discardButton)
					BagRouter(state.bag)
				}
				Row {
					RoundsRemainingRouter(state = state.roundsRemainingState)
					DiscardsRemainingRouter(state = state.discardsRemainingState)
				}
			}
			// Played Area Guide
			val renderedLetters = buildList {
				playedLetters.forEachIndexed { index, letter ->
					if (placeholderIndex == index) add(null) // null = placeholder
					add(letter)
				}
				if (placeholderIndex == playedLetters.size) add(null)
			}

			LazyRow(
				modifier = Modifier
					.offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
					.fillMaxWidth()
					.height(80.dp)
					.background(Color.LightGray)
					.onGloballyPositioned { coords ->
						playedBounds = coords.boundsInParent()
						rowBoundsMap[-1] = coords.boundsInWindow()
					},
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically
			) {
				itemsIndexed(
					items = renderedLetters,
					key = { index, item -> item?.id ?: "placeholder-$index" }
				) { index, letter ->
					if (letter == null) {
						Box(
							modifier = Modifier
								.background(Color.Green)
								.size(48.dp)
								.animateItem()
						)
					} else if (letter.id != draggingLetterId) {
						Box(
							modifier = Modifier
								.background(Color.Red)
								.size(48.dp)
								.animateItem()
								.onGloballyPositioned { coords ->
									val topLeftInWindow = coords.boundsInWindow().topLeft
									val topLeftInRoot = topLeftInWindow - rootOffset
									slotPositions[letter.id] = topLeftInRoot
									letterAreaMap[letter.id] = Area.Played
								}
						)
					}
				}
			}

			Spacer(modifier = Modifier.height(16.dp))

			// Holding Area Guide
			val gridRows = (holdingLetters.size + gridColumns - 1) / gridColumns
			val gridSlots = List(gridRows * gridColumns) { it }

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(Color.Cyan)
					.onGloballyPositioned { coords ->
						holdingBounds = coords.boundsInParent()
					}
			) {
				gridSlots.chunked(gridColumns).forEachIndexed { rowIndex, rowSlots ->
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.onGloballyPositioned { coords ->
								rowBoundsMap[rowIndex] = coords.boundsInWindow()
							},
						horizontalArrangement = Arrangement.Center
					) {
						rowSlots.forEach { index ->
							val letter = holdingLetters.getOrNull(index)
							Box(
								modifier = Modifier
									.size(48.dp)
									.padding(4.dp)
									.onGloballyPositioned { coords ->
										letter?.let {
											val topLeftInWindow = coords.boundsInWindow().topLeft
											val topLeftInRoot = topLeftInWindow - rootOffset
											slotPositions[letter.id] = topLeftInRoot
											letterAreaMap[it.id] = Area.Holding
										}
									}
							)
						}
					}
				}
			}
		}

		// Render all letters at root level
		(holdingLetters + playedLetters).forEach { letter ->
			val slotOffset = slotPositions[letter.id] ?: Offset.Zero

			LetterItem(
				letter = letter,
				targetOffset = slotOffset,
				onClick = {
					println("letter clicked")
					if (letter in holdingLetters) {
						holdingLetters.remove(letter)
						playedLetters.add(letter)
					} else {
						holdingLetters.add(letter)
						playedLetters.remove(letter)
					}
				},
				onDrag = { newOffset ->
					if (draggingLetterId == letter.id) {
						val originOffset = slotPositions[letter.id] ?: Offset.Zero
						val newDraggedOffset = originOffset + newOffset

						// Calculate placeholder index
						val draggedCenterX = newDraggedOffset.x + tileWidthPx / 2

						val insertIndex = playedLetters.indexOfFirst { ref ->
							val centerX = slotPositions[ref.id]?.x?.plus(tileWidthPx / 2) ?: Float.MAX_VALUE
							draggedCenterX < centerX
						}.let { if (it == -1) playedLetters.size else it }

						placeholderIndex = insertIndex
					}
				},
				onDrop = { droppedLetter, dropOffset ->
					val inPlayed = playedBounds?.contains(dropOffset) == true
					val inHolding = holdingBounds?.contains(dropOffset) == true

					when {
						inPlayed -> {
							holdingLetters.remove(droppedLetter)

							val originalIndex = playedLetters.indexOfFirst { it.id == droppedLetter.id }
							val rawIndex = placeholderIndex ?: playedLetters.size

							// If the letter was already in the list and its original index was before the placeholder,
							// subtract 1 to compensate for the shift caused by removal
							val adjustedIndex = if (originalIndex != -1 && originalIndex < rawIndex) {
								rawIndex - 1
							} else {
								rawIndex
							}

							playedLetters.remove(droppedLetter)
							sleep(100L)
							playedLetters.add(adjustedIndex.coerceIn(0, playedLetters.size), droppedLetter)
							letterAreaMap[droppedLetter.id] = Area.Played
						}
						inHolding -> {
							playedLetters.remove(droppedLetter)
							insertLetterByPosition(droppedLetter, holdingLetters, holdingLetters, dropOffset, slotPositions)
							letterAreaMap[droppedLetter.id] = Area.Holding
						}
						else -> {
							val area = letterAreaMap[letter.id] ?: Area.Holding
							if (area == Area.Played) {
								insertLetterByPosition(droppedLetter, playedLetters, playedLetters, dropOffset, slotPositions)
								holdingLetters.remove(droppedLetter)
							} else {
								insertLetterByPosition(droppedLetter, holdingLetters, holdingLetters, dropOffset, slotPositions)
								playedLetters.remove(droppedLetter)
							}
						}
					}

					draggingLetterId = null
					placeholderIndex = null
				},
				onDragStateChanged = {
					draggingLetterId = it
				}
			)
		}
	}
}

internal enum class Area { Holding, Played }

private fun insertLetterByPosition(
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
