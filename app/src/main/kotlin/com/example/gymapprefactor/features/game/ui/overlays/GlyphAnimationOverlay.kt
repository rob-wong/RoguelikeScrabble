package com.example.gymapprefactor.features.game.ui.overlays

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.features.game.presentation.models.animation.GlyphAnimationPayload
import com.example.gymapprefactor.features.game.ui.animation.GlyphDropAnimation
import com.example.gymapprefactor.features.game.ui.animation.GlyphAnimationHandler
import com.example.gymapprefactor.features.game.ui.animation.rememberGlyphAnimationState
import kotlin.random.Random

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun GlyphAnimationOverlay(
	glyphAnimation: GlyphAnimationPayload?,
	onGlyphAnimationComplete: () -> Unit,
	modifier: Modifier = Modifier
) {
	val glyphAnimationState = rememberGlyphAnimationState()

	GlyphAnimationHandler(
		glyphAnimation = glyphAnimation,
		glyphAnimationState = glyphAnimationState,
		onGlyphAnimationComplete = onGlyphAnimationComplete
	)
	
	// Observe Animatable values to trigger recomposition
	var alpha by remember { mutableStateOf(0f) }
	var verticalOffset by remember { mutableStateOf(0f) }
	
	LaunchedEffect(glyphAnimationState.alpha) {
		snapshotFlow { glyphAnimationState.alpha.value }.collect { alpha = it }
	}
	
	LaunchedEffect(glyphAnimationState.verticalOffset) {
		snapshotFlow { glyphAnimationState.verticalOffset.value }.collect { verticalOffset = it }
	}
	
	if (glyphAnimation != null) {
		BoxWithConstraints(
			modifier = modifier.fillMaxSize()
		) {
			val screenHeight = maxHeight
			val screenWidth = maxWidth
			val baseOffset = screenHeight * 0.33f
			
			// Random horizontal offset: +/- 10% of screen width
			val horizontalOffset = remember(glyphAnimation) {
				val randomOffset = (Random.nextFloat() - 0.5f) * 2f // -1.0 to 1.0
				screenWidth * 0.1f * randomOffset
			}

			GlyphDropAnimation(
				glyphAnimation = glyphAnimation,
				alpha = alpha,
				modifier = Modifier
					.align(Alignment.TopCenter)
					.offset(
						x = horizontalOffset,
						y = baseOffset + verticalOffset.dp
					)
			)
		}
	}
}
