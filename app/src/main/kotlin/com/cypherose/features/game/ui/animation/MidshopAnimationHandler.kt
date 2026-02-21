package com.cypherose.features.game.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween

internal suspend fun animateDiscardsShake(
	shakeOffset: Animatable<Float, AnimationVector1D>
) {
	val shakeDuration = 300
	val shakeAmount = 15f
	val shakeCycles = 5
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
