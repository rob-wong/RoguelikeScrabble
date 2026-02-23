package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.interceptors.DrawHandInterceptor
import com.cypherose.business.gameplayLoop.domain.mappers.DrawHandMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.CurrentRound
import com.cypherose.business.models.copy
import javax.inject.Inject

class DrawHandUseCase @Inject constructor(
	private val drawHandMapper: DrawHandMapper,
	@JvmSuppressWildcards private val interceptors: List<DrawHandInterceptor>,
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

		val initialParam = DrawHandMapper.Param(round.mutableDeck, levelStageSeed, drawnAmount)
		val param = runRequestInterceptors(initialParam)
		val result = drawHandMapper.map(param)
		val gameWithNewHand = applyDrawResultToGameState(game, round, result)
		return runResponseInterceptors(gameWithNewHand, result)
	}

	private fun runRequestInterceptors(initialParam: DrawHandMapper.Param): DrawHandMapper.Param {
		return interceptors.sortedBy { it.priority }
			.fold(initialParam) { acc, interceptor -> interceptor.onRequest(acc) }
	}

	private fun runResponseInterceptors(
		gameState: ActiveGameState,
		drawResult: DrawHandMapper.Output
	): ActiveGameState {
		return interceptors.sortedByDescending { it.priority }
			.fold(gameState) { state, interceptor -> interceptor.onResponse(state, drawResult) }
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
