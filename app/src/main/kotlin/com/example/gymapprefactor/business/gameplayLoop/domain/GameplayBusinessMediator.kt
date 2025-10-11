package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.ActiveGameValues
import com.example.gymapprefactor.business.models.ActiveGameVariables
import com.example.gymapprefactor.business.models.CurrentRound
import com.example.gymapprefactor.business.models.GameState
import com.example.gymapprefactor.business.models.copy
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import java.util.UUID

class GameplayBusinessMediator(
	private val getGameStateUseCase: GetGameStateUseCase,
	private val userBusinessMediator: UserBusinessMediator,
	private val saveGameStateUseCase: SaveGameStateUseCase,
	private val endGameUseCase: EndGameUseCase,
	private val drawHandMapper: DrawHandMapper,
) {
	suspend fun fetchOrCreateActiveGame(): ActiveGameState {
		return getGameState() as? ActiveGameState ?: createActiveGame()
	}

	suspend fun getGameState(): GameState {
		return getGameStateUseCase()
	}

	suspend fun drawHand(game: ActiveGameState): ActiveGameState {
		val (variables, values, round) = game
		val (deck, seed) = values.deck to values.seed

		val result = drawHandMapper.map(
			DrawHandMapper.Param(deck, seed, variables.handSize)
		)

		val newGameState = game.copy(
			currentRound = round.copy(
				mutableDeck = deck.copy(
					letters = deck.letters.filter { result.remaining.contains(it) }
				),
				hand = result.drawn
			)
		)

		saveGameStateUseCase(newGameState).also { return newGameState }
	}

	suspend fun endGame(game: ActiveGameState, saveProgression: Boolean) {
		endGameUseCase(game, saveProgression)
	}

	private suspend fun createActiveGame(): ActiveGameState {
		val user = userBusinessMediator.getUser()
		val gameDeck = user.decks
			.first() // change with multiple deck support
			.copy() // during the game, the deck will be changed but not permanently

		val game = ActiveGameState(
			activeGameVariables = ActiveGameVariables(
				glyphCount = 0,
				runesCount = 0,
				stage = STARTING_STAGE,
				level = STARTING_LEVEL,
				maxRounds = STARTING_MAX_ROUNDS,
				handSize = STARTING_HAND_SIZE,
			),
			activeGameValues = ActiveGameValues(
				seed = (System.currentTimeMillis() xor UUID.randomUUID().mostSignificantBits),
				deck = gameDeck,
				effects = mutableListOf(),
			),
			currentRound = CurrentRound(
				round = STARTING_ROUND,
				enemyHealth = 20,
				wordsPlayed = listOf(),
				mutableDeck = gameDeck.copy(),
				hand = listOf(),
			)
		)

		return drawHand(game)
	}
	
	private companion object {
		const val STARTING_STAGE = 1
		const val STARTING_LEVEL = 1
		const val STARTING_ROUND = 1
		const val STARTING_MAX_ROUNDS = 3
		const val STARTING_HAND_SIZE = 6
	}
}
