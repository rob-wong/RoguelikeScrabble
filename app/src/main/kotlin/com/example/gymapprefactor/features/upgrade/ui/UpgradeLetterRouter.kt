package com.example.gymapprefactor.features.upgrade.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.buttons.presentation.ImageButtonState
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.ui.LetterRouter
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState

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
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		LetterRouter(state.letterState)
		ButtonRouter(state.buttonState, Modifier.fillMaxWidth().padding(horizontal = 15.dp))
	}
}

@Composable
@Preview
private fun UpgradeLetterPreview() {
	UpgradeLetterRouter(
		UpgradeLetterState.Content(
			LetterState.Display(DeckType.Default, 'a', 1),
			ImageButtonState.Content({ }, ImageState.UpgradeButton, ImageState.None)
		)
	)
}
