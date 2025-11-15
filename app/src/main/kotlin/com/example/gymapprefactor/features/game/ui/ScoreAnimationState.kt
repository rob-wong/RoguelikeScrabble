package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState

internal class ScoreAnimationState(
	val scoreValueMap: MutableMap<String, Int>,
	val scoreAlphaMap: MutableMap<String, Animatable<Float, AnimationVector1D>>,
	val scoreShakeMap: MutableMap<String, Animatable<Float, AnimationVector1D>>,
	val totalScoreState: MutableState<Int?>,
	val totalScoreAlpha: Animatable<Float, AnimationVector1D>,
	val totalScoreShake: Animatable<Float, AnimationVector1D>,
	val scoredLetters: MutableMap<String, Letter>,
	val orderedScoredLetters: SnapshotStateList<GameScreenState.DraggableLetter>,
	val scoreLetterPositions: MutableMap<String, Offset>,
) {
	var totalScore: Int?
		get() = totalScoreState.value
		set(value) { totalScoreState.value = value }
}

@Composable
internal fun rememberScoreAnimationState(): ScoreAnimationState {
	val scoreValueMap = remember { mutableStateMapOf<String, Int>() }
	val scoreAlphaMap = remember { mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>() }
	val scoreShakeMap = remember { mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>() }
	val totalScoreState = remember { mutableStateOf<Int?>(null) }
	val totalScoreAlpha = remember { Animatable(0f) }
	val totalScoreShake = remember { Animatable(0f) }
	val scoredLetters = remember { mutableStateMapOf<String, Letter>() }
	val orderedScoredLetters = remember { mutableStateListOf<GameScreenState.DraggableLetter>() }
	val scoreLetterPositions = remember { mutableStateMapOf<String, Offset>() }

	return remember {
		ScoreAnimationState(
			scoreValueMap = scoreValueMap,
			scoreAlphaMap = scoreAlphaMap,
			scoreShakeMap = scoreShakeMap,
			totalScoreState = totalScoreState,
			totalScoreAlpha = totalScoreAlpha,
			totalScoreShake = totalScoreShake,
			scoredLetters = scoredLetters,
			orderedScoredLetters = orderedScoredLetters,
			scoreLetterPositions = scoreLetterPositions
		)
	}
}

