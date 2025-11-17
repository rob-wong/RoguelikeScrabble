package com.example.gymapprefactor.features.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gymapprefactor.features.game.presentation.models.MidshopResultPayload

@Composable
internal fun MidshopResultOverlay(
	result: MidshopResultPayload?,
	onAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier
) {
	if (result == null) {
		return
	}
	
	Box(
		modifier = modifier.fillMaxSize()
	) {
		// Dark overlay
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.7f))
		)
		
		when (result) {
			is MidshopResultPayload.Upgrade -> {
				UpgradeResultContent(
					originalLetters = result.originalLetters,
					upgradedLetters = result.upgradedLetters,
					onAnimationComplete = onAnimationComplete
				)
			}
		}
	}
}
