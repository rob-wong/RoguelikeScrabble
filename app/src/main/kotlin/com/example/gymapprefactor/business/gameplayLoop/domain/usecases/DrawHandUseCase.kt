package com.example.gymapprefactor.business.gameplayLoop.domain.usecases

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.DrawHandMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.CurrentRound
import com.example.gymapprefactor.business.models.copy
import javax.inject.Inject

class DrawHandUseCase @Inject constructor(
	private val drawHandMapper: DrawHandMapper
) {
	operator fun invoke(
		drawnAmount: Int,
		game: ActiveGameState
	): ActiveGameState {
		val (variables, values, round) = game
		val levelStageSeed = combineSeedWithLevelAndStage(
			baseSeed = values.seed,
			stage = variables.stage,
			level = variables.level
		)

		val result = drawHandMapper.map(
			DrawHandMapper.Param(round.mutableDeck, levelStageSeed, drawnAmount)
		)

		return applyDrawResultToGameState(game, round, result)
	}
	
	private fun combineSeedWithLevelAndStage(
		baseSeed: Long,
		stage: Int,
		level: Int
	): Long {
		return baseSeed xor (stage.toLong() shl 32) xor (level.toLong() shl 16)
	}

	private fun applyDrawResultToGameState(
		game: ActiveGameState,
		round: CurrentRound,
		result: DrawHandMapper.Output
	): ActiveGameState {
		return game.copy(
			currentRound = round.copy(
				mutableDeck = round.mutableDeck.copy(
					letters = round.mutableDeck.letters.filter { result.remaining.contains(it) }
				),
				hand = round.hand + result.drawn
			)
		)
	}
}
