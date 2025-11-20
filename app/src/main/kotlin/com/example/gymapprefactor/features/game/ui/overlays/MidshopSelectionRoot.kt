package com.example.gymapprefactor.features.game.ui.overlays

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.ui.components.MidshopOptionItem

@Composable
fun MidshopSelectionOverlay(
	options: List<MidshopOption>,
	selectedOption: MidshopOption?,
	confirmButton: ButtonState,
	onOptionSelected: (MidshopOption) -> Unit,
	modifier: Modifier = Modifier,
) {
	BackHandler {
		// Prevent back navigation during midshop selection
	}
	
	MidshopSelectionContent(
		options = options,
		selectedOption = selectedOption,
		confirmButton = confirmButton,
		onOptionSelected = onOptionSelected,
		modifier = modifier
	)
}

@Composable
private fun MidshopSelectionContent(
	options: List<MidshopOption>,
	selectedOption: MidshopOption?,
	confirmButton: ButtonState,
	onOptionSelected: (MidshopOption) -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier.fillMaxSize()
	) {
		MidshopDarkOverlay()
		
		MidshopSelectionUI(
			options = options,
			selectedOption = selectedOption,
			confirmButton = confirmButton,
			onOptionSelected = onOptionSelected
		)
	}
}

@Composable
private fun MidshopDarkOverlay() {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.Black.copy(alpha = 0.7f))
	)
}

@Composable
private fun MidshopSelectionUI(
	options: List<MidshopOption>,
	selectedOption: MidshopOption?,
	confirmButton: ButtonState,
	onOptionSelected: (MidshopOption) -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(32.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		MidshopOptionsGrid(
			options = options,
			selectedOption = selectedOption,
			onOptionSelected = onOptionSelected
		)
		
		MidshopConfirmButton(confirmButton)
	}
}

@Composable
private fun MidshopOptionsGrid(
	options: List<MidshopOption>,
	selectedOption: MidshopOption?,
	onOptionSelected: (MidshopOption) -> Unit,
) {
	val columns = calculateGridColumns(options.size)
	val rows = options.chunked(columns)
	
	LazyColumn(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 32.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		itemsIndexed(
			items = rows,
			key = { index, row ->
				val key = index
				key
			}
		) { index, row ->
			MidshopOptionRow(
				row = row,
				columns = columns,
				selectedOption = selectedOption,
				onOptionSelected = onOptionSelected
			)
		}
	}
}

@Composable
private fun MidshopOptionRow(
	row: List<MidshopOption>,
	columns: Int,
	selectedOption: MidshopOption?,
	onOptionSelected: (MidshopOption) -> Unit,
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
		row.forEach { option ->
			val modifier = calculateOptionModifier(rowWidth, columns)
			MidshopOptionItem(
				option = option,
				isSelected = selectedOption?.id == option.id,
				onClick = { onOptionSelected(option) },
				modifier = modifier
			)
		}
	}
}

@Composable
private fun MidshopConfirmButton(confirmButton: ButtonState) {
	ButtonRouter(
		state = confirmButton,
		modifier = Modifier
			.size(80.dp)
			.padding(top = 16.dp)
	)
}

@Composable
private fun calculateOptionModifier(
	rowWidth: Dp,
	columns: Int
): Modifier {
	// Calculate width based on what it would be in a full row
	// Account for padding (32dp * 2 = 64dp total), item padding (4dp * 2 = 8dp per item), and spacing (8dp between items)
	val horizontalPadding = 64.dp // 32dp on each side
	val itemPadding = 8.dp // 4dp on each side of each item
	val spacingBetweenItems = (8 * (columns - 1)).dp // spacing between items
	val availableWidth = rowWidth - horizontalPadding
	val itemWidthValue = (availableWidth.value - spacingBetweenItems.value) / columns - itemPadding.value
	val itemWidth = itemWidthValue.dp.coerceAtLeast(80.dp) // Minimum 80dp
	return Modifier.width(itemWidth)
}

private fun calculateGridColumns(optionCount: Int): Int {
	return when {
		optionCount <= 2 -> 2
		optionCount <= 4 -> 2
		optionCount <= 6 -> 3
		optionCount <= 9 -> 3
		else -> 4
	}
}
