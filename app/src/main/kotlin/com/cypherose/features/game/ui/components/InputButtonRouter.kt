package com.cypherose.features.game.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.cypherose.app.util.DeviceUtil
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.features.game.presentation.models.GameScreenState
import com.cypherose.features.game.presentation.models.InputButtonState

@Composable
internal fun InputButtonRouter(
	state: InputButtonState,
	playedLetters: SnapshotStateList<GameScreenState.DraggableLetter>,
	modifier: Modifier = Modifier,
) {
	when (state) {
		is InputButtonState.InputWordButton -> InputWordButton(state, playedLetters, modifier)
		else -> Unit
	}
}

@Composable
private fun InputWordButton(
	state: InputButtonState.InputWordButton,
	playedLetters: SnapshotStateList<GameScreenState.DraggableLetter>,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.widthIn(max = buttonWidthRouter())
			.wrapContentSize()
			.clickable(
				onClick = {
					state.onClick(playedLetters.map { it.id })
				}
			),
		contentAlignment = Alignment.Center
	) {
		ImageRouter(
			state = state.image,
			modifier = Modifier.fillMaxWidth()
		)
	}
}

@Composable
private fun buttonWidthRouter(): Dp {
	return when (DeviceUtil.isLandscape) {
		true -> DeviceUtil.getColumnWidthDp(5)
		false -> DeviceUtil.getColumnWidthDp(4)
	}
}
