package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.gymapprefactor.features.game.presentation.models.EffectAnimationPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun EffectAnimationHandler(
	effectAnimations: List<EffectAnimationPayload>?,
	effectState: EffectAnimationState,
	scoreState: ScoreAnimationState,
	onEffectAnimationComplete: () -> Unit
) {
	LaunchedEffect(effectAnimations) {
		if (effectAnimations != null && effectAnimations.isNotEmpty()) {
			// Process each effect animation in order
			for (effectAnimation in effectAnimations) {
				animateEffect(effectAnimation, effectState, scoreState)
				delay(100) // Small delay between effects
			}
		}

		onEffectAnimationComplete()
	}
}

private suspend fun animateEffect(
	effectAnimation: EffectAnimationPayload,
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

