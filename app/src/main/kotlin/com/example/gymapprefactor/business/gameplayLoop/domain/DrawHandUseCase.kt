package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.copy
import javax.inject.Inject

class DrawHandUseCase @Inject constructor(
	private val drawHandMapper: DrawHandMapper
) {
	operator fun invoke(
		drawnAmount: Int,
		game: ActiveGameState
	): ActiveGameState {
		val (_, values, round) = game
		val (_, seed) = values.deck to values.seed

		val result = drawHandMapper.map(
			DrawHandMapper.Param(round.mutableDeck, seed, drawnAmount)
		)
		val newGameState = game.copy(
			currentRound = round.copy(
				mutableDeck = round.mutableDeck.copy(
					letters = round.mutableDeck.letters.filter { result.remaining.contains(it) }
				),
				hand = round.hand + result.drawn
			)
		)

		return newGameState
	}
}
