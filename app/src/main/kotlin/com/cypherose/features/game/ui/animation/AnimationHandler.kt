package com.cypherose.features.game.ui.animation

import androidx.compose.runtime.Composable
import com.cypherose.features.game.presentation.models.animation.EffectAnimationPayload
import com.cypherose.features.game.presentation.models.animation.ScoreAnimationPayload
import com.cypherose.features.game.ui.LetterBoardState

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
	onEffectAnimationComplete: () -> Unit,
	onEffectAnimationStart: (effectId: String) -> Unit
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
		onEffectAnimationStart = onEffectAnimationStart,
		onEffectAnimationComplete = {
			onEffectAnimationConsumed()
			onEffectAnimationComplete()
		}
	)
}
