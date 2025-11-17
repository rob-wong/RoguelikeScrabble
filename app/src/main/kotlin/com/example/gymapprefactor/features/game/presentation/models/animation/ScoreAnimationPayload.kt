package com.example.gymapprefactor.features.game.presentation.models.animation

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.LetterScore
import com.example.gymapprefactor.business.models.Letter

data class ScoreAnimationPayload(
	val letterScores: List<LetterScore>,
	val letters: List<Letter>
)
