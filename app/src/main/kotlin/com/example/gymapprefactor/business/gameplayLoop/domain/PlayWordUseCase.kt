package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.gameplayLoop.domain.models.GameplayExceptions
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.DefaultEffect
import com.example.gymapprefactor.business.models.Letter
import java.util.UUID
import javax.inject.Inject

class PlayWordUseCase @Inject constructor(
	private val wordValidityMapper: WordValidityMapper,
	private val drawHandUseCase: DrawHandUseCase,
	private val scoreWordMapper: ScoreWordMapper,
	private val saveGameStateUseCase: SaveGameStateUseCase,
) {
	suspend operator fun invoke(
		letters: List<Letter>,
		game: ActiveGameState
	): Result<ScoredWordResult> {
		val wordAsString = letters.map { it.letter }.joinToString(separator = "")

		return if (wordValidityMapper.map(letters)) {
			val wordEffect = createWordEffect(wordAsString)
			val gameWithoutLetters = removeLettersAndUpdateRound(game, letters, wordAsString)
			val gameWithNewHand = drawHandUseCase(letters.size, gameWithoutLetters)
			val savedGame = saveGameStateUseCase(gameWithNewHand) as ActiveGameState
			val letterScores = scoreWordMapper.map(
				ScoreWordMapper.Param(
					letters = letters,
					activeGameValues = savedGame.activeGameValues
				)
			)

			Result.success(
				ScoredWordResult(
					gameState = savedGame,
					letterScores = letterScores,
					letters = letters,
					wordEffect = wordEffect
				)
			)
		} else {
			Result.failure(
				exception = GameplayExceptions.InvalidWord(word = wordAsString)
			)
		}
	}

	private fun createWordEffect(wordAsString: String): DefaultEffect {
		return DefaultEffect(
			id = UUID.randomUUID().toString(),
			label = wordAsString.uppercase()
		)
	}

	private fun removeLettersAndUpdateRound(
		game: ActiveGameState,
		letters: List<Letter>,
		wordAsString: String
	): ActiveGameState {
		return game.copy(
			currentRound = game.currentRound.copy(
				hand = game.currentRound.hand.filterNot { letters.contains(it) },
				round = game.currentRound.round + 1,
				wordsPlayed = game.currentRound.wordsPlayed + wordAsString
			)
		)
	}
}
