package com.example.gymapprefactor.features.game.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.gymapprefactor.features.game.presentation.models.animation.EffectAnimationPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun EffectAnimationHandler(
	effectAnimations: List<EffectAnimationPayload>?,
	effectState: EffectAnimationState,
	scoreState: ScoreAnimationState,
	onEffectAnimationStart: (effectId: String) -> Unit,
	onEffectAnimationComplete: () -> Unit
) {
	LaunchedEffect(effectAnimations) {
		if (effectAnimations != null && effectAnimations.isNotEmpty()) {
			// Longest animation duration (alpha animation: 200ms + 200ms + 150ms = 550ms)
			val longestAnimationDuration = 550L
			
			coroutineScope {
				for ((index, effectAnimation) in effectAnimations.withIndex()) {
					launch {
						when (index) {
							0 -> {
								// First animation: start immediately
								onEffectAnimationStart(effectAnimation.effectId)
								animateEffect(effectAnimation, effectState, scoreState)
							}
							in 1..3 -> {
								// Indices 1-3: wait for previous animation to finish
								// Each waits cumulative: index 1 waits 550ms, index 2 waits 1100ms, index 3 waits 1650ms
								val delayMs = index * longestAnimationDuration
								delay(delayMs)
								onEffectAnimationStart(effectAnimation.effectId)
								animateEffect(effectAnimation, effectState, scoreState)
							}
							else -> {
								// Index 4+: progressive delay based on when previous started
								// Calculate cumulative delay from start
								val delayMs = calculateCumulativeDelay(index, longestAnimationDuration)
								delay(delayMs)
								onEffectAnimationStart(effectAnimation.effectId)
								animateEffect(effectAnimation, effectState, scoreState)
							}
						}
					}
				}
			}
			
			// Wait 1 second after all effect animations complete before triggering damage animation
			delay(1000L)
		}

		onEffectAnimationComplete()
	}
}

private suspend fun animateEffect(
	effectAnimation: EffectAnimationPayload,
	effectState: EffectAnimationState,
	scoreState: ScoreAnimationState
) {
	when (effectAnimation) {
		is EffectAnimationPayload.Score -> animateScoreEffect(effectAnimation, effectState, scoreState)
		is EffectAnimationPayload.Glyph -> animateGlyphEffect(effectAnimation, effectState)
	}
}

private suspend fun animateScoreEffect(
	effectAnimation: EffectAnimationPayload.Score,
	effectState: EffectAnimationState,
	scoreState: ScoreAnimationState
) {
	coroutineScope {
		// 1. Small shake on the individual effect
		launch {
			val shakeAnim = effectState.effectShakeMap.getOrPut(effectAnimation.effectId) {
				Animatable(0f)
			}
			animateEffectShake(shakeAnim)
		}

		// 2. Show "+n" or "x n" text on the effect
		launch {
			val alphaAnim = effectState.effectScoreAlphaMap.getOrPut(effectAnimation.effectId) {
				Animatable(0f)
			}
			// Store scoreDelta for addition effects, multiplier for multiplication effects
			if (effectAnimation.multiplier != null) {
				effectState.effectMultiplierMap[effectAnimation.effectId] = effectAnimation.multiplier
				// Clear scoreDelta for multiplication effects
				effectState.effectScoreValueMap.remove(effectAnimation.effectId)
			} else {
				effectState.effectScoreValueMap[effectAnimation.effectId] = effectAnimation.scoreDelta
				// Clear multiplier for non-multiplication effects
				effectState.effectMultiplierMap.remove(effectAnimation.effectId)
			}
			// Clear glyph amount for score effects
			effectState.effectGlyphAmountMap.remove(effectAnimation.effectId)
			alphaAnim.snapTo(0f)
			alphaAnim.animateTo(1f, tween(durationMillis = 200, easing = LinearEasing))
			delay(200)
			alphaAnim.animateTo(0f, tween(durationMillis = 150, easing = LinearEasing))
		}

		// 3. Update and shake total score in scoring lane
		launch {
			// Update both scoreState and effectState for display logic
			scoreState.totalScore = effectAnimation.cumulativeScore
			effectState.totalScore = effectAnimation.cumulativeScore
			// Ensure alpha stays at 1f so score remains visible
			scoreState.totalScoreAlpha.snapTo(1f)
			// Use effectState's shake for the animation
			animateTotalScoreShake(effectState.totalScoreShake)
		}
	}
}

private suspend fun animateGlyphEffect(
	effectAnimation: EffectAnimationPayload.Glyph,
	effectState: EffectAnimationState
) {
	coroutineScope {
		// 1. Small shake on the individual effect
		launch {
			val shakeAnim = effectState.effectShakeMap.getOrPut(effectAnimation.effectId) {
				Animatable(0f)
			}
			animateEffectShake(shakeAnim)
		}

		// 2. Show "+<amount><glyph icon>" text on the effect
		launch {
			val alphaAnim = effectState.effectScoreAlphaMap.getOrPut(effectAnimation.effectId) {
				Animatable(0f)
			}
			// Store glyph amount for display
			effectState.effectGlyphAmountMap[effectAnimation.effectId] = effectAnimation.glyphAmount
			// Clear score-related maps for glyph effects
			effectState.effectScoreValueMap.remove(effectAnimation.effectId)
			effectState.effectMultiplierMap.remove(effectAnimation.effectId)
			alphaAnim.snapTo(0f)
			alphaAnim.animateTo(1f, tween(durationMillis = 200, easing = LinearEasing))
			delay(200)
			alphaAnim.animateTo(0f, tween(durationMillis = 150, easing = LinearEasing))
		}
	}
}

private suspend fun animateEffectShake(
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

private suspend fun animateTotalScoreShake(
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

/**
 * Calculates cumulative delay from the start for effect animations.
 * 
 * - Index 0: 0ms (starts immediately)
 * - Index 1: waits for index 0 to finish (550ms), then starts
 * - Index 2+: progressive delay based on when previous STARTED
 *   - Index 2: starts 200ms after index 1 started (550ms + 200ms = 750ms)
 *   - Index 3: starts 180ms after index 2 started (750ms + 180ms = 930ms)
 *   - Index 4: starts 160ms after index 3 started (930ms + 160ms = 1090ms)
 *   - ...decreasing by 20ms each time...
 *   - Minimum 100ms between starts
 * 
 * @param index The animation index (0-based)
 * @param longestAnimationDuration Duration of longest animation in ms (550ms)
 * @return Cumulative delay from start in milliseconds
 */
private fun calculateCumulativeDelay(index: Int, longestAnimationDuration: Long): Long {
	return when (index) {
		0 -> 0L
		in 1..3 -> index * longestAnimationDuration // Wait for previous to finish
		else -> {
			// Index 3 starts at 3 * longestAnimationDuration (1650ms)
			// Index 4+ start based on when previous STARTED + progressive delay
			// Index 4 starts at index 3 start time (1650ms) + 200ms = 1850ms
			var previousStartTime = 3 * longestAnimationDuration // Index 3 start time (1650ms)
			for (i in 4..index) {
				// Progressive delay: starts at 200ms for index 4, decreases by 5ms, minimum 150ms
				val stepDelay = 200L - (i - 4) * 5L
				val delay = stepDelay.coerceAtLeast(150L)
				previousStartTime += delay
			}
			previousStartTime
		}
	}
}
