package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
internal fun InvalidWordShakeHandler(
	invalidWordTrigger: Boolean,
	shakeOffset: Animatable<Float, AnimationVector1D>,
	shakeMutex: Mutex,
	onInvalidWordConsumed: () -> Unit
) {
	LaunchedEffect(invalidWordTrigger) {
		shakeMutex.withLock {
			if (invalidWordTrigger) {
				shakeOffset.snapTo(20f)
				shakeOffset.animateTo(
					targetValue = 0f,
					animationSpec = repeatable(
						iterations = 4,
						animation = tween(durationMillis = 50),
						repeatMode = RepeatMode.Reverse
					)
				)
			}
		}
		onInvalidWordConsumed()
	}
}

@Composable
internal fun LevelAdvanceShakeHandler(
	levelAdvanceShakeTrigger: Boolean,
	holdingAreaShakeOffset: Animatable<Float, AnimationVector1D>,
	bagShakeOffset: Animatable<Float, AnimationVector1D>,
	shakeMutex: Mutex,
	onLevelAdvanceShakeConsumed: () -> Unit
) {
	LaunchedEffect(levelAdvanceShakeTrigger) {
		shakeMutex.withLock {
			if (levelAdvanceShakeTrigger) {
				holdingAreaShakeOffset.snapTo(20f)
				bagShakeOffset.snapTo(20f)
				coroutineScope {
					launch {
						holdingAreaShakeOffset.animateTo(
							targetValue = 0f,
							animationSpec = repeatable(
								iterations = 4,
								animation = tween(durationMillis = 50),
								repeatMode = RepeatMode.Reverse
							)
						)
					}
					launch {
						bagShakeOffset.animateTo(
							targetValue = 0f,
							animationSpec = repeatable(
								iterations = 4,
								animation = tween(durationMillis = 50),
								repeatMode = RepeatMode.Reverse
							)
						)
					}
				}
			}
		}
		onLevelAdvanceShakeConsumed()
	}
}


