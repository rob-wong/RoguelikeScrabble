package com.cypherose.features.game.ui.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cypherose.features.game.presentation.models.PreviouslyPlayedEffectItem

@Composable
fun PreviouslyPlayedOverlayRoot(
	visible: Boolean,
	effects: List<PreviouslyPlayedEffectItem>,
	onDismiss: () -> Unit,
) {
	AnimatedVisibility(
		visible = visible,
		enter = slideInHorizontally(initialOffsetX = { it }),
		exit = slideOutHorizontally(targetOffsetX = { it }),
		modifier = Modifier
			.fillMaxSize()
			.zIndex(3f)
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			// Backdrop - clickable area outside the overlay
			Box(
				modifier = Modifier
					.fillMaxSize()
					.clickable { onDismiss() }
			)
			
			// Overlay panel - positioned over buttons (zIndex 3f vs buttons' 2f)
			PreviouslyPlayedOverlay(
				effects = effects,
				onDismiss = onDismiss,
				modifier = Modifier.align(Alignment.CenterEnd)
			)
		}
	}
}

@Composable
private fun PreviouslyPlayedOverlay(
	effects: List<PreviouslyPlayedEffectItem>,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxHeight()
			.fillMaxWidth(0.7f)
			.background(Color.DarkGray)
			.padding(16.dp)
	) {
		Text(
			text = "Previously Played",
			color = Color.White,
			modifier = Modifier.padding(bottom = 16.dp)
		)
		LazyColumn {
			items(effects, key = { it.label }) { item ->
				PreviouslyPlayedEffectRow(
					label = item.label,
					matchPercent = item.matchPercent,
					modifier = Modifier.padding(vertical = 4.dp)
				)
			}
		}
		DismissButton(onClick = onDismiss)
	}
}

@Composable
private fun PreviouslyPlayedEffectRow(
	label: String,
	matchPercent: Float,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier) {
		Text(text = label, color = Color.White)
		Text(
			text = "${(matchPercent * 100).toInt()}% match",
			color = Color.Gray
		)
	}
}

@Composable
private fun DismissButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Text(
		text = "Close",
		color = Color.White,
		modifier = modifier
			.padding(top = 16.dp)
			.clickable { onClick() }
	)
}
