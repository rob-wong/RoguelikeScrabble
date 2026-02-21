package com.cypherose.features.game.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.cypherose.features.game.presentation.models.GameScreenState

internal class LetterBoardState(
	val holdingLetters: SnapshotStateList<GameScreenState.DraggableLetter>,
	val playedLetters: SnapshotStateList<GameScreenState.DraggableLetter>,
	val slotPositions: MutableMap<String, Offset>,
	val letterAreaMap: MutableMap<String, Area>,
	val placeholderIndexState: MutableState<Int?>,
	val playedBoundsState: MutableState<Rect?>,
	val holdingBoundsState: MutableState<Rect?>,
	val rootOffsetState: MutableState<Offset>,
	val draggingLetterIdState: MutableState<String?>,
	val rowBoundsMap: MutableMap<Int, Rect>,
	val tileWidthPx: Float,
) {
	var placeholderIndex: Int?
		get() = placeholderIndexState.value
		set(value) { placeholderIndexState.value = value }
	
	var playedBounds: Rect?
		get() = playedBoundsState.value
		set(value) { playedBoundsState.value = value }
	
	var holdingBounds: Rect?
		get() = holdingBoundsState.value
		set(value) { holdingBoundsState.value = value }
	
	var rootOffset: Offset
		get() = rootOffsetState.value
		set(value) { rootOffsetState.value = value }
	
	var draggingLetterId: String?
		get() = draggingLetterIdState.value
		set(value) { draggingLetterIdState.value = value }
}

@Composable
internal fun rememberLetterBoardState(
	initialLetters: List<GameScreenState.DraggableLetter>,
	tileWidthPx: Float
): LetterBoardState {
	val holdingLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }
	val playedLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }
	val slotPositions = remember { mutableStateMapOf<String, Offset>() }
	val letterAreaMap = remember { mutableStateMapOf<String, Area>() }
	val placeholderIndexState = remember { mutableStateOf<Int?>(null) }
	val playedBoundsState = remember { mutableStateOf<Rect?>(null) }
	val holdingBoundsState = remember { mutableStateOf<Rect?>(null) }
	val rootOffsetState = remember { mutableStateOf(Offset.Zero) }
	val draggingLetterIdState = remember { mutableStateOf<String?>(null) }
	val rowBoundsMap = remember { mutableStateMapOf<Int, Rect>() }

	// Update holding letters when initial letters change
	LaunchedEffect(initialLetters) {
		holdingLetters.clear()
		holdingLetters.addAll(initialLetters.map { it.copy() })
		playedLetters.clear()
		letterAreaMap.clear()
		slotPositions.keys.retainAll(holdingLetters.map { it.id })
	}

	return remember {
		LetterBoardState(
			holdingLetters = holdingLetters,
			playedLetters = playedLetters,
			slotPositions = slotPositions,
			letterAreaMap = letterAreaMap,
			placeholderIndexState = placeholderIndexState,
			playedBoundsState = playedBoundsState,
			holdingBoundsState = holdingBoundsState,
			rootOffsetState = rootOffsetState,
			draggingLetterIdState = draggingLetterIdState,
			rowBoundsMap = rowBoundsMap,
			tileWidthPx = tileWidthPx
		)
	}
}

