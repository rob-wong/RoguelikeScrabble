package com.cypherose.features.upgrade.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.buttons.presentation.ImageButtonState
import com.cypherose.common.components.buttons.ui.ButtonRouter
import com.cypherose.common.components.presentation.DeckType
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.LetterState
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.common.components.ui.LetterRouter
import com.cypherose.common.components.ui.OutlinedText
import com.cypherose.features.upgrade.presentation.models.SelectedLetterState
import com.cypherose.ui.theme.common

@Composable
fun LetterSelectionOverlay(
	state: SelectedLetterState,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier.fillMaxSize()
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.7f))
				.clickable(onClick = state.onDismiss)
		)
		
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.Center)
				.padding(horizontal = 40.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(40.dp)
		) {
			Row(
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically
			) {
				OutlinedText(
					text = state.cost.toString(),
					textAlign = TextAlign.Center,
					textStyle = common,
					outlineWidth = 7,
					useGlow = false
				)
				ImageRouter(
					state = ImageState.Basic.RuneIcon,
					modifier = Modifier
						.padding(start = 16.dp)
						.size(48.dp)
				)
			}
			
			LetterRouter(
				state = LetterState.Display(
					type = DeckType.Default,
					letter = state.letter.letter.toUpperCase(),
					level = state.letter.level
				)
			)
			
			ButtonRouter(
				state = if (state.canAfford) {
					ImageButtonState.Content(
						onClick = state.onUpgrade,
						background = ImageState.Basic.UpgradeButton,
						foreground = ImageState.None
					)
				} else {
					ImageButtonState.None
				},
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}
