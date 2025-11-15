package com.example.gymapprefactor.features.game.ui

import androidx.compose.runtime.Composable
import com.example.gymapprefactor.features.game.presentation.models.EffectAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.ScoreAnimationPayload

@Composable
internal fun AnimationHandler(
	scoreBreakdown: ScoreAnimationPayload?,
	effectAnimations: List<EffectAnimationPayload>?,
	scoreState: ScoreAnimationState,
	effectState: EffectAnimationState,
	boardState: LetterBoardState,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit,
	onEffectAnimationConsumed: () -> Unit,
	onEffectAnimationComplete: () -> Unit
) {
	// Handle score animations (letter scores)
	ScoreAnimationHandler(
		scoreBreakdown = scoreBreakdown,
		scoreState = scoreState,
		boardState = boardState,
		onScoreAnimationConsumed = onScoreAnimationConsumed,
		onScoreAnimationComplete = onScoreAnimationComplete
	)

	// Handle effect animations
	EffectAnimationHandler(
		effectAnimations = effectAnimations,
		effectState = effectState,
		scoreState = scoreState,
		onEffectAnimationComplete = {
			onEffectAnimationConsumed()
			onEffectAnimationComplete()
		}
	)
}
