package com.example.gymapprefactor.features.game.ui.overlays.midshopresults

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ImageRouter
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.features.game.ui.animation.animateDiscardsShake
import com.example.gymapprefactor.ui.theme.common
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
internal fun PersistenceResultContent(
	oldMaxRounds: Int,
	newMaxRounds: Int,
	onAnimationComplete: () -> Unit
) {
	var showNewValue by remember { mutableStateOf(false) }
	val shakeAnimation = remember { Animatable(0f) }
	
	LaunchedEffect(Unit) {
		delay(500)
		
		// Shake animation
		showNewValue = true
		animateDiscardsShake(shakeAnimation)
		
		// Wait a bit more before completing
		delay(500)
		onAnimationComplete()
	}
	
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Box(
			modifier = Modifier
				.offset { 
					IntOffset(
						shakeAnimation.value.roundToInt(), 
						0
					) 
				},
			contentAlignment = Alignment.Center
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				ImageRouter(
					state = ImageState.PlaysLeftIcon,
					modifier = Modifier.width(70.dp)
				)
				OutlinedText(
					text = if (showNewValue) newMaxRounds.toString() else oldMaxRounds.toString(),
					textAlign = TextAlign.Center,
					textStyle = common,
					outlineWidth = 5,
					useGlow = false,
				)
			}
		}
	}
}

