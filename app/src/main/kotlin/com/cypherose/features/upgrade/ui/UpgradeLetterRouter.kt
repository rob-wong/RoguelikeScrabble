package com.cypherose.features.upgrade.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cypherose.common.components.ui.LetterRouter
import com.cypherose.features.upgrade.presentation.models.UpgradeLetterState

@Composable
internal fun UpgradeLetterRouter(
	state: UpgradeLetterState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is UpgradeLetterState.Content -> UpgradeLetterContent(state, modifier)
		is UpgradeLetterState.None -> Unit
	}
}

@Composable
private fun UpgradeLetterContent(
	state: UpgradeLetterState.Content,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.then(
				if (state.isClickable) {
					Modifier.clickable(onClick = { state.onLetterClick(state.cost) })
				} else {
					Modifier
				}
			),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		LetterRouter(state.letterState)
	}
}
