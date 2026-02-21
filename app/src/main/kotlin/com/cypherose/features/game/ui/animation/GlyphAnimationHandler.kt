package com.cypherose.features.game.ui.animation

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.cypherose.features.game.presentation.models.animation.GlyphAnimationPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun GlyphAnimationHandler(
	glyphAnimation: GlyphAnimationPayload?,
	glyphAnimationState: GlyphAnimationState,
	onGlyphAnimationComplete: () -> Unit
) {
	val onComplete by rememberUpdatedState(onGlyphAnimationComplete)
	
	LaunchedEffect(glyphAnimation) {
		if (glyphAnimation == null) {
			return@LaunchedEffect
		}

		glyphAnimationState.alpha.snapTo(0f)
		glyphAnimationState.verticalOffset.snapTo(0f)

		// Fade in
		glyphAnimationState.alpha.animateTo(1f, tween(durationMillis = 200))
		
		// Hold
		delay(300L)

		// Float up and fade out simultaneously
		// Fade out faster so it's invisible before exiting the screen
		coroutineScope {
			launch {
				glyphAnimationState.alpha.animateTo(0f, tween(durationMillis = 600))
			}
			launch {
				glyphAnimationState.verticalOffset.animateTo(-200f, tween(durationMillis = 800))
			}
		}

		// Wait for animations to complete
		delay(800L)
		onComplete()
	}
}
