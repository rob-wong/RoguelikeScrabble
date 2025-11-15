package com.example.gymapprefactor.features.game.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import com.example.gymapprefactor.features.game.presentation.models.ScoreAnimationPayload
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@SuppressWarnings("MaxLineLength")
internal fun ScoreAnimationHandler(
	scoreBreakdown: ScoreAnimationPayload?,
	scoreState: ScoreAnimationState,
	boardState: LetterBoardState,
	onScoreAnimationConsumed: () -> Unit,
	onScoreAnimationComplete: () -> Unit
) {
	LaunchedEffect(scoreBreakdown) {
		if (scoreBreakdown == null || scoreBreakdown.letterScores.isEmpty()) {
			println("LetterBoard: scoreBreakdown empty -> $scoreBreakdown")
			return@LaunchedEffect
		}

		println(
			"LetterBoard: incoming scores=${scoreBreakdown.letterScores.map { it.first to it.second }} " +
				"holding=${boardState.holdingLetters.map { it.id }} played=${boardState.playedLetters.map { it.id }}"
		)

		initializeScoreState(scoreBreakdown, scoreState, boardState)
		val filteredScores = filterValidScores(scoreBreakdown, scoreState)

		if (filteredScores.isEmpty()) {
			println("LetterBoard: filteredScores empty, skipping animation")
			onScoreAnimationConsumed()
			clearLetterScoreState(scoreState)
			return@LaunchedEffect
		}

		animateLetterScores(filteredScores, scoreState)
		animateTotalScore(scoreState)
		// Don't clear totalScore - let it persist for effect animations
		clearLetterScoreState(scoreState)

		onScoreAnimationConsumed()
		onScoreAnimationComplete()
	}
}

private fun initializeScoreState(
	scoreBreakdown: ScoreAnimationPayload,
	scoreState: ScoreAnimationState,
	boardState: LetterBoardState
) {
	scoreState.scoredLetters.clear()
	scoreState.scoreLetterPositions.clear()
	scoreState.orderedScoredLetters.clear()
	scoreBreakdown.letters.forEach { letter ->
		scoreState.scoredLetters[letter.id] = letter
		scoreState.orderedScoredLetters.add(
			GameScreenState.DraggableLetter(
				id = letter.id,
				letterState = LetterState.Display(
					type = DeckType.Default,
					letter = letter.letter.uppercaseChar(),
					level = letter.level
				)
			)
		)
		scoreState.scoreLetterPositions[letter.id] =
			boardState.slotPositions[letter.id] ?: Offset.Zero
	}
}

private fun filterValidScores(
	scoreBreakdown: ScoreAnimationPayload,
	scoreState: ScoreAnimationState
): List<Pair<String, Int>> {
	val filteredScores = scoreBreakdown.letterScores.filter { score ->
		scoreState.scoredLetters.containsKey(score.first)
	}
	println("LetterBoard: filteredScores=${filteredScores.map { it.first }}")
	return filteredScores
}

private suspend fun animateLetterScores(
	filteredScores: List<Pair<String, Int>>,
	scoreState: ScoreAnimationState
) {
	scoreState.totalScore = filteredScores.sumOf { it.second }
	scoreState.totalScoreAlpha.snapTo(0f)
	scoreState.totalScoreShake.snapTo(0f)
	scoreState.scoreValueMap.clear()

	for ((letterId, _) in filteredScores) {
		scoreState.scoreAlphaMap.getOrPut(letterId) { Animatable(0f) }.snapTo(0f)
		scoreState.scoreShakeMap.getOrPut(letterId) { Animatable(0f) }.snapTo(0f)
	}

	for ((letterId, score) in filteredScores) {
		scoreState.scoreValueMap[letterId] = score
		animateLetterScore(letterId, scoreState)
		delay(320L)
	}

	delay(240L)

	for ((letterId, _) in filteredScores) {
		val alphaAnim = scoreState.scoreAlphaMap[letterId] ?: continue
		alphaAnim.animateTo(0f, tween(durationMillis = 180))
	}
}

private suspend fun animateLetterScore(
	letterId: String,
	scoreState: ScoreAnimationState
) {
	coroutineScope {
		launch {
			val shakeAnim = scoreState.scoreShakeMap.getOrPut(letterId) { Animatable(0f) }
			shakeAnim.snapTo(0f)
			repeat(2) {
				shakeAnim.animateTo(12f, tween(durationMillis = 70))
				shakeAnim.animateTo(-12f, tween(durationMillis = 70))
			}
			shakeAnim.animateTo(0f, tween(durationMillis = 80))
		}
		launch {
			val alphaAnim = scoreState.scoreAlphaMap.getOrPut(letterId) { Animatable(0f) }
			alphaAnim.snapTo(0f)
			alphaAnim.animateTo(1f, tween(durationMillis = 250))
		}
	}
}

private suspend fun animateTotalScore(scoreState: ScoreAnimationState) {
	scoreState.totalScoreAlpha.snapTo(0f)
	scoreState.totalScoreAlpha.animateTo(1f, tween(durationMillis = 280))
	repeat(2) {
		scoreState.totalScoreShake.animateTo(14f, tween(durationMillis = 90))
		scoreState.totalScoreShake.animateTo(-14f, tween(durationMillis = 90))
	}
	scoreState.totalScoreShake.animateTo(0f, tween(durationMillis = 90))
	// Keep alpha at 1f - don't fade out, effects will update the score
}

private fun clearLetterScoreState(scoreState: ScoreAnimationState) {
	// Clear letter-specific score state, but keep totalScore for effect animations
	scoreState.scoreValueMap.clear()
	scoreState.orderedScoredLetters.clear()
	scoreState.scoreLetterPositions.clear()
}


