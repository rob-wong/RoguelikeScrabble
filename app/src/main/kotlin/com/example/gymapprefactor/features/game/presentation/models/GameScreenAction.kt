package com.example.gymapprefactor.features.game.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.models.Letter

sealed class GameScreenAction : Action {
	data class StartPlaying(
		val glyphCount: Int,
		val runesCount: Int,
		val onQuitPressed: () -> Unit,
		val onWordPlayed: (List<String>) -> Unit,
		val onDiscardPressed: () -> Unit,
		val hand: List<Letter>,
		val currentLettersInDeck: Int,
		val maxLettersInDeck: Int,
	) : GameScreenAction()

	data object None : GameScreenAction()
}
