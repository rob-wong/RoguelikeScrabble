package com.cypherose.features.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.buttons.ui.ButtonRouter
import com.cypherose.common.components.ui.ResourceBarRouter
import com.cypherose.features.game.ui.components.EnemyHealthBarRouter
import com.cypherose.features.game.presentation.models.animation.EffectAnimationPayload
import com.cypherose.features.game.presentation.models.animation.ScoreAnimationPayload
import com.cypherose.features.game.presentation.models.GameScreenState

@Composable
fun MainGameContent(
	state: GameScreenState.Playing,
	invalidWordTrigger: Boolean,
	onInvalidWordConsumed: () -> Unit,
	levelAdvanceShakeTrigger: Boolean,
	onLevelAdvanceShakeConsumed: () -> Unit,
	scoreBreakdown: ScoreAnimationPayload?,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit,
	effectAnimations: List<EffectAnimationPayload>?,
	onEffectAnimationConsumed: () -> Unit,
	onEffectAnimationComplete: () -> Unit,
) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		ResourceBarRouter(state.resourceBar)
		EnemyHealthBarRouter(state.enemyHealthBarState)
		Column(Modifier.fillMaxSize()) {
			Spacer(Modifier.height(50.dp))
			LetterBoard(
				state = state,
				invalidWordTrigger = invalidWordTrigger,
				onInvalidWordConsumed = onInvalidWordConsumed,
				levelAdvanceShakeTrigger = levelAdvanceShakeTrigger,
				onLevelAdvanceShakeConsumed = onLevelAdvanceShakeConsumed,
				scoreBreakdown = scoreBreakdown,
				onScoreAnimationConsumed = onScoreAnimationConsumed,
				onScoreAnimationComplete = onScoreAnimationComplete,
				effectAnimations = effectAnimations,
				onEffectAnimationConsumed = onEffectAnimationConsumed,
				onEffectAnimationComplete = onEffectAnimationComplete,
			)
		}
	}
}

@Composable
fun TopRightButtons(
	state: GameScreenState.Playing,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.End
	) {
		ButtonRouter(state.quitButton, Modifier.size(40.dp))
		Spacer(Modifier.height(8.dp))
		Box(
			modifier = Modifier
				.size(40.dp)
				.background(Color.Red)
				.clickable { state.onPreviouslyPlayedPressed() }
		)
	}
}
