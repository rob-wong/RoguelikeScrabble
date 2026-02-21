package com.cypherose.features.upgrade.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cypherose.common.components.ui.animations.LetterUpgradeAnimation
import com.cypherose.features.upgrade.presentation.models.UpgradeAnimationPayload

@Composable
fun UpgradeAnimationOverlay(
	payload: UpgradeAnimationPayload,
	onAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier.fillMaxSize()
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.7f))
		)
		
		LetterUpgradeAnimation(
			originalLetters = listOf(payload.originalLetter),
			upgradedLetters = listOf(payload.upgradedLetter),
			onAnimationComplete = onAnimationComplete
		)
	}
}
