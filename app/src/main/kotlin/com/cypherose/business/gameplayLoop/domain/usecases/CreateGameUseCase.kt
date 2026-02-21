package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.mappers.EnemyCreationMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.ActiveGameValues
import com.cypherose.business.models.ActiveGameVariables
import com.cypherose.business.models.CurrentRound
import com.cypherose.business.models.copy
import com.cypherose.business.user.domain.UserBusinessMediator
import java.util.UUID
import javax.inject.Inject

/**
 * Creates a new active game with starting values.
 * This encapsulates all the logic for initializing a new game.
 */
class CreateGameUseCase @Inject constructor(
	private val userBusinessMediator: UserBusinessMediator,
	private val drawHandUseCase: DrawHandUseCase,
	private val saveGameStateUseCase: SaveGameStateUseCase,
	private val enemyCreationMapper: EnemyCreationMapper,
) {
	suspend operator fun invoke(): ActiveGameState {
		val user = userBusinessMediator.getUser().getOrThrow()
		val gameDeck = user.decks
			.first()
			.copy()

		val initialEnemyHealth = enemyCreationMapper.map(
			EnemyCreationMapper.Param(
				stage = STARTING_STAGE,
				level = STARTING_LEVEL
			)
		)

		val game = ActiveGameState(
			activeGameVariables = ActiveGameVariables(
				glyphCount = 0,
				runesCount = user.runesCount,
				startingRunesCount = user.runesCount,
				stage = STARTING_STAGE,
				level = STARTING_LEVEL,
				maxRounds = STARTING_MAX_ROUNDS,
				maxDiscards = STARTING_MAX_DISCARDS,
				handSize = STARTING_HAND_SIZE,
				gameLost = false,
				needsEffectSelection = false,
			),
			activeGameValues = ActiveGameValues(
				seed = (System.currentTimeMillis() xor UUID.randomUUID().mostSignificantBits),
				deck = gameDeck,
				effects = mutableListOf(),
			),
			currentRound = CurrentRound(
				round = STARTING_ROUND,
				discardsUsed = STARTING_DISCARDS_USED,
				enemyHealth = initialEnemyHealth,
				wordsPlayed = listOf(),
				mutableDeck = gameDeck.copy(),
				hand = listOf(),
				effects = emptyList(),
			)
		)

		val initializedGame = drawHandUseCase(
			drawnAmount = game.activeGameVariables.handSize,
			game = game
		)

		saveGameStateUseCase(initializedGame)
		return initializedGame
	}

	private companion object {
		const val STARTING_STAGE = 1
		const val STARTING_LEVEL = 1
		const val STARTING_ROUND = 1
		const val STARTING_DISCARDS_USED = 0
		const val STARTING_MAX_ROUNDS = 3
		const val STARTING_MAX_DISCARDS = 3
		const val STARTING_HAND_SIZE = 8
	}
}
