package com.cypherose.features.game.presentation.models.animation

import com.cypherose.business.gameplayLoop.domain.mappers.LetterScore
import com.cypherose.business.models.Letter

data class ScoreAnimationPayload(
	val letterScores: List<LetterScore>,
	val letters: List<Letter>
)
