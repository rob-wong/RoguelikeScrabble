package com.cypherose.features.game.ui.overlays.midshopresults

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cypherose.business.models.Letter
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.buttons.ui.ButtonRouter
import com.cypherose.features.game.ui.components.AwakenLetterItem

@Composable
fun AwakenLetterSelectionOverlay(
	letters: List<Letter>,
	selectedLetter: Letter?,
	confirmButton: ButtonState,
	modifier: Modifier = Modifier,
	onLetterSelected: (Letter) -> Unit,
	onConfirmClicked: (() -> Unit)? = null,
) {
	BackHandler {
		// Prevent back navigation during letter selection
	}
	
	AwakenLetterSelectionContent(
		letters = letters,
		selectedLetter = selectedLetter,
		confirmButton = confirmButton,
		onLetterSelected = onLetterSelected,
		onConfirmClicked = onConfirmClicked,
		modifier = modifier
	)
}

@Composable
private fun AwakenLetterSelectionContent(
	letters: List<Letter>,
	selectedLetter: Letter?,
	confirmButton: ButtonState,
	modifier: Modifier = Modifier,
	onLetterSelected: (Letter) -> Unit,
	onConfirmClicked: (() -> Unit)? = null,
) {
	Box(
		modifier = modifier.fillMaxSize()
	) {
		AwakenLetterSelectionUI(
			letters = letters,
			selectedLetter = selectedLetter,
			confirmButton = confirmButton,
			onLetterSelected = onLetterSelected,
			onConfirmClicked = onConfirmClicked
		)
	}
}

@Composable
private fun AwakenLetterSelectionUI(
	letters: List<Letter>,
	selectedLetter: Letter?,
	confirmButton: ButtonState,
	onLetterSelected: (Letter) -> Unit,
	onConfirmClicked: (() -> Unit)? = null,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(32.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		AwakenLettersGrid(
			letters = letters,
			selectedLetter = selectedLetter,
			onLetterSelected = onLetterSelected
		)
		
		AwakenConfirmButton(
			confirmButton = confirmButton,
			onConfirmClicked = onConfirmClicked
		)
	}
}

@Composable
private fun AwakenLettersGrid(
	letters: List<Letter>,
	selectedLetter: Letter?,
	onLetterSelected: (Letter) -> Unit,
) {
	val columns = calculateGridColumns(letters.size)
	val rows = letters.chunked(columns)
	
	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 32.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		itemsIndexed(
			items = rows,
			key = { index, _ -> index }
		) { _, row ->
			AwakenLetterRow(
				row = row,
				columns = columns,
				selectedLetter = selectedLetter,
				onLetterSelected = onLetterSelected
			)
		}
	}
}

@Composable
private fun AwakenLetterRow(
	row: List<Letter>,
	columns: Int,
	selectedLetter: Letter?,
	onLetterSelected: (Letter) -> Unit,
) {
	val density = LocalDensity.current
	var rowWidth by remember { mutableStateOf(0.dp) }
	
	Row(
		horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
		modifier = Modifier
			.fillMaxWidth()
			.onGloballyPositioned { coordinates ->
				rowWidth = with(density) { coordinates.size.width.toDp() }
			}
	) {
		row.forEach { letter ->
			val modifier = calculateLetterModifier(rowWidth, columns)
			AwakenLetterItem(
				letter = letter,
				isSelected = selectedLetter?.id == letter.id,
				onClick = { onLetterSelected(letter) },
				modifier = modifier
			)
		}
	}
}

@Composable
private fun AwakenConfirmButton(
	confirmButton: ButtonState,
	onConfirmClicked: (() -> Unit)? = null,
) {
	val wrappedButtonState = if (onConfirmClicked != null && confirmButton is IconButtonState.Content) {
		IconButtonState.Content(
			onClick = onConfirmClicked,
			image = confirmButton.image
		)
	} else {
		confirmButton
	}
	
	ButtonRouter(
		state = wrappedButtonState,
		modifier = Modifier
			.size(80.dp)
			.padding(top = 16.dp)
	)
}

@Composable
private fun calculateLetterModifier(
	rowWidth: Dp,
	columns: Int
): Modifier {
	val horizontalPadding = 64.dp
	val itemPadding = 8.dp
	val spacingBetweenItems = (8 * (columns - 1)).dp
	val availableWidth = rowWidth - horizontalPadding
	val itemWidthValue = (availableWidth.value - spacingBetweenItems.value) / columns - itemPadding.value
	val itemWidth = itemWidthValue.dp.coerceAtLeast(80.dp)
	return Modifier.width(itemWidth)
}

private fun calculateGridColumns(letterCount: Int): Int {
	return when {
		letterCount <= 2 -> 2
		letterCount <= 4 -> 2
		letterCount <= 6 -> 3
		letterCount <= 9 -> 3
		else -> 4
	}
}
