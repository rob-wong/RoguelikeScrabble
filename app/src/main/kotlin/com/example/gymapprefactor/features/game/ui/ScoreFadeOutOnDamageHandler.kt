package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
internal fun ScoreFadeOutOnDamageHandler(
	currentHealth: Int?,
	scoreState: ScoreAnimationState,
	effectState: EffectAnimationState
) {
	var previousHealth by remember { mutableIntStateOf(0) }

	LaunchedEffect(currentHealth) {
		if (currentHealth == null) {
			return@LaunchedEffect
		}

		val healthDecreased = currentHealth < previousHealth
		if (healthDecreased && scoreState.totalScore != null) {
			// Wait a bit for the health bar animation to start, then fade out the score
			delay(100) // Small delay to let damage animation start
			fadeOutScore(scoreState, effectState)
		}

		previousHealth = currentHealth
	}
}

private suspend fun fadeOutScore(
	scoreState: ScoreAnimationState,
	effectState: EffectAnimationState
) {
	// Fade out the score after damage is applied
	scoreState.totalScoreAlpha.animateTo(0f, tween(durationMillis = 200, easing = LinearEasing))
	// Clear the score values after fade completes
	delay(200)
	scoreState.totalScore = null
	effectState.totalScore = null
}

