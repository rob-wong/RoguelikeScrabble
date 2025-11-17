package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.common.components.ui.LetterRouter
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

@Composable
internal fun UpgradeResultContent(
	originalLetters: List<Letter>,
	upgradedLetters: List<Letter>,
	onAnimationComplete: () -> Unit
) {
	// Track which letters have been upgraded (by index)
	var upgradedIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
	
	val shakeAnimations = upgradedLetters.map { 
		remember { Animatable(0f) }
	}
	
	LaunchedEffect(Unit) {
		// Pause for 1 second before starting the first animation
		delay(1000)
		
		coroutineScope {
			// Animate letters sequentially with 0.3s delay between each
			upgradedLetters.forEachIndexed { index, _ ->
				launch {
					delay(index * 300L)
					upgradedIndices = upgradedIndices + index
					animateLetterShake(shakeAnimations[index])
				}
			}
		}
		
		// coroutineScope waits for all animations to complete, then wait a bit more
		delay(500)
		onAnimationComplete()
	}
	
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(32.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			upgradedLetters.forEachIndexed { index, upgradedLetter ->
				val letter = if (upgradedIndices.contains(index)) upgradedLetter else originalLetters[index]
				Box(
					modifier = Modifier
						.wrapContentSize(Alignment.Center)
						.offset { 
							IntOffset(
								shakeAnimations[index].value.roundToInt(), 
								0
							) 
						},
					contentAlignment = Alignment.Center
				) {
					LetterRouter(
						state = LetterState.Display(
							type = DeckType.Default,
							letter = letter.letter.toUpperCase(),
							level = letter.level
						)
					)
				}
			}
		}
	}
}

private suspend fun animateLetterShake(
	shakeOffset: Animatable<Float, AnimationVector1D>
) {
	val shakeDuration = 300
	val shakeAmount = 6f
	val shakeCycles = 3
	val cycleDuration = shakeDuration / shakeCycles

	shakeOffset.snapTo(0f)
	shakeOffset.animateTo(
		targetValue = shakeAmount,
		animationSpec = tween(
			durationMillis = cycleDuration / 2,
			easing = LinearEasing
		)
	)
	shakeOffset.animateTo(
		targetValue = -shakeAmount,
		animationSpec = tween(
			durationMillis = cycleDuration,
			easing = LinearEasing
		)
	)
	shakeOffset.animateTo(
		targetValue = shakeAmount,
		animationSpec = tween(
			durationMillis = cycleDuration,
			easing = LinearEasing
		)
	)
	shakeOffset.animateTo(
		targetValue = 0f,
		animationSpec = tween(
			durationMillis = cycleDuration / 2,
			easing = LinearEasing
		)
	)
}
