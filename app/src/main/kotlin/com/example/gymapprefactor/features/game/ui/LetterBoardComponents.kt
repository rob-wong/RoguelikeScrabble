@file:Suppress("TooManyFunctions")

package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.gymapprefactor.common.components.ui.LetterRouter
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.common.components.ui.letterFontRouter
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.ui.animation.EffectAnimationState
import com.example.gymapprefactor.features.game.ui.animation.ScoreAnimationState
import kotlin.math.roundToInt

@Composable
internal fun ScoreLane(
	scoreState: ScoreAnimationState,
	effectState: EffectAnimationState?,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
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
				items = scoreState.orderedScoredLetters,
				key = { _, letter -> letter.id }
			) { index, letter ->
				val shake = scoreState.scoreShakeMap[letter.id]?.value ?: 0f
				val alpha = scoreState.scoreAlphaMap[letter.id]?.value ?: 0f
				val score = scoreState.scoreValueMap[letter.id]
				val fontLevel = scoreState.scoredLetters[letter.id]?.level ?: 1
				val font = letterFontRouter(fontLevel)

				Box(
					modifier = Modifier
						.size(48.dp)
						.padding(4.dp),
					contentAlignment = Alignment.Center
				) {
					if (score != null) {
						OutlinedText(
							text = "+$score",
							textAlign = TextAlign.Center,
							textStyle = font.copy(
								fontSize = font.fontSize * 0.66
							),
							modifier = Modifier
								.offset { IntOffset(shake.roundToInt(), 0) }
								.graphicsLayer(alpha = alpha),
							outlineWidth = 5,
							useGlow = false
						)
					}
				}
			}
		}

		// Show total score from effectState if available, otherwise from scoreState
		val totalScore = effectState?.totalScore ?: scoreState.totalScore
		totalScore?.let { total ->
			TotalScoreDisplay(total, scoreState, effectState)
		}
	}
}

@Composable
private fun BoxScope.TotalScoreDisplay(
	total: Int,
	scoreState: ScoreAnimationState,
	effectState: EffectAnimationState?
) {
	val totalFontLevel = scoreState.orderedScoredLetters.maxOfOrNull {
		scoreState.scoredLetters[it.id]?.level ?: 1
	} ?: 1
	val shakeOffset = effectState?.totalScoreShake?.value ?: scoreState.totalScoreShake.value
	// Always use scoreState's alpha - it's kept at 1f after initial animation and during effects
	val alpha = scoreState.totalScoreAlpha.value
	OutlinedText(
		text = "+$total",
		textAlign = TextAlign.Center,
		textStyle = letterFontRouter(totalFontLevel),
		modifier = Modifier
			.align(Alignment.Center)
			.offset { IntOffset(shakeOffset.roundToInt(), 0) }
			.graphicsLayer(alpha = alpha),
		outlineWidth = 7,
		useGlow = false
	)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlayedArea(
	boardState: LetterBoardState,
	shakeOffset: Animatable<Float, AnimationVector1D>,
	modifier: Modifier = Modifier
) {
	val renderedLetters = buildList {
		boardState.playedLetters.forEachIndexed { index, letter ->
			if (boardState.placeholderIndex == index) add(null)
			add(letter)
		}
		if (boardState.placeholderIndex == boardState.playedLetters.size) add(null)
	}
	LazyRow(
		modifier = modifier
			.offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
			.fillMaxWidth()
			.height(80.dp)
			.background(Color.LightGray)
			.onGloballyPositioned { coords ->
				boardState.playedBounds = coords.boundsInParent()
				boardState.rowBoundsMap[-1] = coords.boundsInWindow()
			},
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		itemsIndexed(
			items = renderedLetters,
			key = { index, item ->
				val key = item?.id ?: "placeholder-$index"
				key
			}
		) { index, letter ->
			if (letter == null) {
				Box(
					modifier = Modifier.size(48.dp)
				)
			} else if (letter.id != boardState.draggingLetterId) {
				Box(
					modifier = Modifier
						.animateItem()
						.size(48.dp)
						.onGloballyPositioned { coords ->
							val topLeftInWindow = coords.boundsInWindow().topLeft
							val topLeftInRoot = topLeftInWindow - boardState.rootOffset
							boardState.slotPositions[letter.id] = topLeftInRoot
							boardState.letterAreaMap[letter.id] = Area.Played
						}
				)
			}
		}
	}
}

@Composable
internal fun HoldingArea(
	boardState: LetterBoardState,
	gridColumns: Int,
	shakeOffset: Animatable<Float, AnimationVector1D>,
	modifier: Modifier = Modifier
) {
	val gridRows = (boardState.holdingLetters.size + gridColumns - 1) / gridColumns
	val gridSlots = List(gridRows * gridColumns) { it }

	Column(
		modifier = modifier
			.offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
			.fillMaxWidth()
			.background(Color.Cyan)
			.onGloballyPositioned { coords ->
				boardState.holdingBounds = coords.boundsInParent()
			}
	) {
		gridSlots.chunked(gridColumns).forEachIndexed { rowIndex, rowSlots ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.onGloballyPositioned { coords ->
						boardState.rowBoundsMap[rowIndex] = coords.boundsInWindow()
					},
				horizontalArrangement = Arrangement.Center
			) {
				rowSlots.forEach { index ->
					val letter = boardState.holdingLetters.getOrNull(index)
					Box(
						modifier = Modifier
							.size(48.dp)
							.padding(4.dp)
							.onGloballyPositioned { coords ->
								letter?.let {
									val topLeftInWindow = coords.boundsInWindow().topLeft
									val topLeftInRoot = topLeftInWindow - boardState.rootOffset
									boardState.slotPositions[letter.id] = topLeftInRoot
									boardState.letterAreaMap[it.id] = Area.Holding
								}
							}
					)
				}
			}
		}
	}
}

@Composable
internal fun LetterOverlays(
	boardState: LetterBoardState,
	scoreState: ScoreAnimationState,
	modifier: Modifier = Modifier
) {
	// Render all letters at root level
	(boardState.holdingLetters + boardState.playedLetters).forEach { letter ->
		val slotOffset = boardState.slotPositions[letter.id] ?: Offset.Zero

		LetterItem(
			letter = letter,
			targetOffset = slotOffset,
			onClick = { handleLetterClick(letter, boardState) },
			onDrag = { newOffset -> handleLetterDrag(letter, newOffset, boardState) },
			onDrop = { droppedLetter, dropOffset -> handleLetterDrop(droppedLetter, dropOffset, letter, boardState) },
			onDragStateChanged = { boardState.draggingLetterId = it }
		)
	}

	// Render scored letter overlays
	scoreState.orderedScoredLetters.forEach { letter ->
		val overlayOffset = scoreState.scoreLetterPositions[letter.id] ?: return@forEach
		Box(
			modifier = modifier
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

private fun handleLetterClick(
	letter: GameScreenState.DraggableLetter,
	boardState: LetterBoardState
) {
	println("letter clicked")
	if (letter in boardState.holdingLetters) {
		boardState.holdingLetters.remove(letter)
		boardState.playedLetters.add(letter)
	} else {
		boardState.holdingLetters.add(letter)
		boardState.playedLetters.remove(letter)
	}
}

private fun handleLetterDrag(
	letter: GameScreenState.DraggableLetter,
	newOffset: Offset,
	boardState: LetterBoardState
) {
	if (boardState.draggingLetterId == letter.id) {
		val originOffset = boardState.slotPositions[letter.id] ?: Offset.Zero
		val newDraggedOffset = originOffset + newOffset

		// Calculate placeholder index
		val draggedCenterX = newDraggedOffset.x + boardState.tileWidthPx / 2

		val insertIndex = boardState.playedLetters.indexOfFirst { ref ->
			val centerX = boardState.slotPositions[ref.id]?.x
				?.plus(boardState.tileWidthPx / 2) ?: Float.MAX_VALUE
			draggedCenterX < centerX
		}.let { if (it == -1) boardState.playedLetters.size else it }

		boardState.placeholderIndex = insertIndex
	}
}

private fun handleLetterDrop(
	droppedLetter: GameScreenState.DraggableLetter,
	dropOffset: Offset,
	letter: GameScreenState.DraggableLetter,
	boardState: LetterBoardState
) {
	val inPlayed = boardState.playedBounds?.contains(dropOffset) == true
	val inHolding = boardState.holdingBounds?.contains(dropOffset) == true

	when {
		inPlayed -> handleDropInPlayedArea(droppedLetter, boardState)
		inHolding -> handleDropInHoldingArea(droppedLetter, dropOffset, boardState)
		else -> handleDropInOtherArea(droppedLetter, letter, dropOffset, boardState)
	}

	boardState.draggingLetterId = null
	boardState.placeholderIndex = null
}

private fun handleDropInPlayedArea(
	droppedLetter: GameScreenState.DraggableLetter,
	boardState: LetterBoardState
) {
	boardState.holdingLetters.remove(droppedLetter)

	val originalIndex = boardState.playedLetters.indexOfFirst { it.id == droppedLetter.id }
	val rawIndex = boardState.placeholderIndex ?: boardState.playedLetters.size

	// If the letter was already in the list and its original index was before the placeholder,
	// subtract 1 to compensate for the shift caused by removal
	val adjustedIndex = if (originalIndex != -1 && originalIndex < rawIndex) {
		rawIndex - 1
	} else {
		rawIndex
	}

	boardState.playedLetters.remove(droppedLetter)
	Thread.sleep(100L)
	boardState.playedLetters.add(
		index = adjustedIndex.coerceIn(0, boardState.playedLetters.size),
		element = droppedLetter
	)
	boardState.letterAreaMap[droppedLetter.id] = Area.Played
}

private fun handleDropInHoldingArea(
	droppedLetter: GameScreenState.DraggableLetter,
	dropOffset: Offset,
	boardState: LetterBoardState
) {
	boardState.playedLetters.remove(droppedLetter)
	insertLetterByPosition(
		droppedLetter,
		boardState.holdingLetters,
		boardState.holdingLetters,
		dropOffset,
		boardState.slotPositions
	)
	boardState.letterAreaMap[droppedLetter.id] = Area.Holding
}

private fun handleDropInOtherArea(
	droppedLetter: GameScreenState.DraggableLetter,
	letter: GameScreenState.DraggableLetter,
	dropOffset: Offset,
	boardState: LetterBoardState
) {
	val area = boardState.letterAreaMap[letter.id] ?: Area.Holding
	if (area == Area.Played) {
		insertLetterByPosition(
			droppedLetter,
			boardState.playedLetters,
			boardState.playedLetters,
			dropOffset,
			boardState.slotPositions
		)
		boardState.holdingLetters.remove(droppedLetter)
	} else {
		insertLetterByPosition(
			droppedLetter,
			boardState.holdingLetters,
			boardState.holdingLetters,
			dropOffset,
			boardState.slotPositions
		)
		boardState.playedLetters.remove(droppedLetter)
	}
}

