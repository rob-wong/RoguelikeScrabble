package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal class GlyphAnimationState(
	val alpha: Animatable<Float, AnimationVector1D>,
	val verticalOffset: Animatable<Float, AnimationVector1D>
) {
	suspend fun clear() {
		alpha.snapTo(0f)
		verticalOffset.snapTo(0f)
	}
}

@Composable
internal fun rememberGlyphAnimationState(): GlyphAnimationState {
	val alpha = remember { Animatable(0f) }
	val verticalOffset = remember { Animatable(0f) }
	return remember {
		GlyphAnimationState(
			alpha = alpha,
			verticalOffset = verticalOffset
		)
	}
}
