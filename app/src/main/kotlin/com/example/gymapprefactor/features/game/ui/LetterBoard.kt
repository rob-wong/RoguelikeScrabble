package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.ui.BagRouter
import com.example.gymapprefactor.common.components.ui.LetterRouter
import com.example.gymapprefactor.common.components.ui.letterFontRouter
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.viewmodel.ScoreAnimationPayload
import com.example.gymapprefactor.features.game.ui.components.DiscardsRemainingRouter
import com.example.gymapprefactor.features.game.ui.components.RoundsRemainingRouter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
	scoreBreakdown: ScoreAnimationPayload?,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier
) {
	val tileWidthPx = with(LocalDensity.current) { 48.dp.toPx() }
	val holdingLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }
	val playedLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }

	val slotPositions = remember { mutableStateMapOf<String, Offset>() }
	val letterAreaMap = remember { mutableStateMapOf<String, Area>() }

	LaunchedEffect(state.letters) {
		holdingLetters.clear()
		holdingLetters.addAll(state.letters.map { it.copy() })
		playedLetters.clear()
		letterAreaMap.clear()
		slotPositions.keys.retainAll(holdingLetters.map { it.id })
	}

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
	val scoreValueMap = remember { mutableStateMapOf<String, Int>() }
	val scoreAlphaMap = remember { mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>() }
	val scoreShakeMap = remember { mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>() }
	var totalScore by remember { mutableStateOf<Int?>(null) }
	val totalScoreAlpha = remember { Animatable(0f) }
	val totalScoreShake = remember { Animatable(0f) }
	val scoredLetters = remember { mutableStateMapOf<String, Letter>() }
	val orderedScoredLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }
	val scoreLetterPositions = remember { mutableStateMapOf<String, Offset>() }

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

	LaunchedEffect(scoreBreakdown) {
		if (scoreBreakdown == null || scoreBreakdown.letterScores.isEmpty()) {
			println("LetterBoard: scoreBreakdown empty -> $scoreBreakdown")
			return@LaunchedEffect
		}

		println(
			"LetterBoard: incoming scores=${scoreBreakdown.letterScores.map { it.first to it.second }} " +
				"holding=${holdingLetters.map { it.id }} played=${playedLetters.map { it.id }}"
		)

		scoredLetters.clear()
		scoreLetterPositions.clear()
		orderedScoredLetters.clear()
		scoreBreakdown.letters.forEach { letter ->
			scoredLetters[letter.id] = letter
			orderedScoredLetters.add(
				GameScreenState.DraggableLetter(
					id = letter.id,
					letterState = LetterState.Display(
						type = DeckType.Default,
						letter = letter.letter.uppercaseChar(),
						level = letter.level
					)
				)
			)
			scoreLetterPositions[letter.id] = slotPositions[letter.id] ?: Offset.Zero
		}

		val filteredScores = scoreBreakdown.letterScores.filter { score ->
			scoredLetters.containsKey(score.first)
		}

		println("LetterBoard: filteredScores=${filteredScores.map { it.first }}")

		if (filteredScores.isEmpty()) {
			println("LetterBoard: filteredScores empty, skipping animation")
			onScoreAnimationConsumed()
			orderedScoredLetters.clear()
			scoreLetterPositions.clear()
			return@LaunchedEffect
		}

		totalScore = filteredScores.sumOf { it.second }
		totalScoreAlpha.snapTo(0f)
		totalScoreShake.snapTo(0f)
		scoreValueMap.clear()

		for ((letterId, _) in filteredScores) {
			scoreAlphaMap.getOrPut(letterId) { Animatable(0f) }.snapTo(0f)
			scoreShakeMap.getOrPut(letterId) { Animatable(0f) }.snapTo(0f)
		}

		for ((letterId, score) in filteredScores) {
			scoreValueMap[letterId] = score

			coroutineScope {
				launch {
					val shakeAnim = scoreShakeMap.getOrPut(letterId) { Animatable(0f) }
					shakeAnim.snapTo(0f)
					repeat(2) {
						shakeAnim.animateTo(12f, tween(durationMillis = 70))
						shakeAnim.animateTo(-12f, tween(durationMillis = 70))
					}
					shakeAnim.animateTo(0f, tween(durationMillis = 80))
				}
				launch {
					val alphaAnim = scoreAlphaMap.getOrPut(letterId) { Animatable(0f) }
					alphaAnim.snapTo(0f)
					alphaAnim.animateTo(1f, tween(durationMillis = 250))
				}
			}

			delay(320L)
		}

		delay(240L)

		for ((letterId, _) in filteredScores) {
			val alphaAnim = scoreAlphaMap[letterId] ?: continue
			alphaAnim.animateTo(0f, tween(durationMillis = 180))
		}

		totalScoreAlpha.snapTo(0f)
		totalScoreAlpha.animateTo(1f, tween(durationMillis = 280))
		repeat(2) {
			totalScoreShake.animateTo(14f, tween(durationMillis = 90))
			totalScoreShake.animateTo(-14f, tween(durationMillis = 90))
		}
		totalScoreShake.animateTo(0f, tween(durationMillis = 90))
		delay(300L)
		totalScoreAlpha.animateTo(0f, tween(durationMillis = 200))
		totalScore = null
		scoreValueMap.clear()
		orderedScoredLetters.clear()
		scoreLetterPositions.clear()

		onScoreAnimationConsumed()
		onScoreAnimationComplete()
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

			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp)
			) {
				LazyRow(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 12.dp),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically
				) {
					itemsIndexed(
						items = orderedScoredLetters,
						key = { index, letter -> letter.id }
					) { index, letter ->
						val shake = scoreShakeMap[letter.id]?.value ?: 0f
						val alpha = scoreAlphaMap[letter.id]?.value ?: 0f
						val score = scoreValueMap[letter.id]
						val fontLevel = scoredLetters[letter.id]?.level ?: 1

						Box(
							modifier = Modifier
								.size(48.dp)
								.padding(4.dp),
							contentAlignment = Alignment.Center
						) {
							if (score != null) {
								Text(
									text = "+$score",
									fontSize = 18.sp,
									fontWeight = FontWeight.Bold,
									style = letterFontRouter(fontLevel),
									modifier = Modifier
										.offset { IntOffset(shake.roundToInt(), 0) }
										.graphicsLayer(alpha = alpha)
								)
							}
						}
					}
				}

				totalScore?.let { total ->
					val totalFontLevel = orderedScoredLetters.maxOfOrNull { scoredLetters[it.id]?.level ?: 1 } ?: 1
					Text(
						text = "+$total",
						fontSize = 22.sp,
						fontWeight = FontWeight.ExtraBold,
						style = letterFontRouter(totalFontLevel),
						modifier = Modifier
							.align(Alignment.Center)
							.offset { IntOffset(totalScoreShake.value.roundToInt(), 0) }
							.graphicsLayer(alpha = totalScoreAlpha.value)
					)
				}
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

		orderedScoredLetters.forEach { letter ->
			val overlayOffset = scoreLetterPositions[letter.id] ?: return@forEach
			Box(
				modifier = Modifier
					.offset {
						IntOffset(
							overlayOffset.x.roundToInt(),
							overlayOffset.y.roundToInt()
						)
					}
					.zIndex(0.5f)
			) {
				LetterRouter(letter.letterState)
			}
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
