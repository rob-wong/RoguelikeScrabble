package com.example.gymapprefactor.features.game.presentation.viewmodel

import com.example.gymapprefactor.business.gameplayLoop.domain.LetterScore
import com.example.gymapprefactor.business.models.Letter

data class ScoreAnimationPayload(
	val letterScores: List<LetterScore>,
	val letters: List<Letter>
)

