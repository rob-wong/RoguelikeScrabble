package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.gameplayLoop.domain.models.GameplayExceptions
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.ActiveGameValues
import com.example.gymapprefactor.business.models.ActiveGameVariables
import com.example.gymapprefactor.business.models.CurrentRound
import com.example.gymapprefactor.business.models.GameState
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.models.copy
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import java.util.UUID

class GameplayBusinessMediator(
	private val getGameStateUseCase: GetGameStateUseCase,
	private val userBusinessMediator: UserBusinessMediator,
	private val saveGameStateUseCase: SaveGameStateUseCase,
	private val endGameUseCase: EndGameUseCase,
	private val drawHandMapper: DrawHandMapper,
	private val wordValidityMapper: WordValidityMapper,
) {
	suspend fun fetchOrCreateActiveGame(): ActiveGameState {
		return getGameState() as? ActiveGameState ?: createActiveGame()
	}

	suspend fun getGameState(): GameState {
		return getGameStateUseCase()
	}

	fun drawHand(drawnAmount: Int, game: ActiveGameState): ActiveGameState {
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

	// turn this into a mapper maybe
	suspend fun onWordPlayed(list: List<Letter>, game: ActiveGameState): Result<ActiveGameState> {
		println("played word: ${list.map { it.letter }}")
		val wordAsString = list.map { it.letter }.joinToString(separator = "")

		return if (wordValidityMapper.map(list)) {
			val newGameState =
				drawHand(
					drawnAmount = list.size,
					game = game.copy(
						currentRound = game.currentRound.copy(
							hand = game.currentRound.hand.filterNot { list.contains(it) },
							round = game.currentRound.round + 1,
							wordsPlayed = game.currentRound.wordsPlayed + wordAsString,
							// TODO enemy health in the enemy update
						)
					),
				)

			saveGameStateUseCase(newGameState)
			Result.success(newGameState)
		} else {
			println("played word: invalid")
			Result.failure(
				exception = GameplayExceptions.InvalidWord(
					word = wordAsString
				)
			)
		}
	}

	suspend fun discardHand(game: ActiveGameState): ActiveGameState {
		val newGameState = drawHand(
			drawnAmount = game.activeGameVariables.handSize,
			game = game.copy(
				currentRound = game.currentRound.copy(
					hand = emptyList()
				)
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

		val initializedGame = drawHand(drawnAmount = game.activeGameVariables.handSize, game)

		saveGameStateUseCase(initializedGame).also { return initializedGame }
	}
	
	private companion object {
		const val STARTING_STAGE = 1
		const val STARTING_LEVEL = 1
		const val STARTING_ROUND = 1
		const val STARTING_MAX_ROUNDS = 3
		const val STARTING_HAND_SIZE = 8
	}
}
