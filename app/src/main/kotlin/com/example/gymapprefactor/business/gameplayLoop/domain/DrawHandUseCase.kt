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
		val (variables, values, round) = game
		val baseSeed = values.seed
		
		// Combine base seed with level and stage for deterministic, level-specific hands
		val levelStageSeed = combineSeedWithLevelAndStage(
			baseSeed = baseSeed,
			stage = variables.stage,
			level = variables.level
		)

		val result = drawHandMapper.map(
			DrawHandMapper.Param(round.mutableDeck, levelStageSeed, drawnAmount)
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
	
	private fun combineSeedWithLevelAndStage(
		baseSeed: Long,
		stage: Int,
		level: Int
	): Long {
		// Combine seed with stage and level using bit operations for deterministic results
		// This ensures different levels/stages have different hands, but same seed+level+stage = same hands
		return baseSeed xor (stage.toLong() shl 32) xor (level.toLong() shl 16)
	}
}
