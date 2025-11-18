package com.example.gymapprefactor.features.game.ui.overlays.midshopresults

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.features.game.presentation.models.MidshopResultPayload

@Composable
internal fun MidshopResultOverlay(
	result: MidshopResultPayload?,
	onAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier,
	selectedAwakenLetter: Letter? = null,
	awakenConfirmButton: ButtonState? = null,
	onAwakenLetterSelected: ((Letter) -> Unit)? = null,
	onAwakenConfirmed: (() -> Unit)? = null,
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
		is MidshopResultPayload.Awaken -> {
			if (onAwakenLetterSelected != null &&
				onAwakenConfirmed != null &&
				awakenConfirmButton != null
				) {
				AwakenLetterSelectionOverlay(
					letters = result.generatedLetters,
					selectedLetter = selectedAwakenLetter,
					confirmButton = awakenConfirmButton,
					onLetterSelected = onAwakenLetterSelected,
					onConfirmClicked = {
						onAnimationComplete()
						onAwakenConfirmed()
					}
				)
			}
		}
	}
	}
}
