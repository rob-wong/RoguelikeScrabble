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
		println("played word: ${letters.map { it.letter }}")
		val wordAsString = letters.map { it.letter }.joinToString(separator = "")

		return if (wordValidityMapper.map(letters)) {
			// Create effect from the played word
			val wordEffect = DefaultEffect(
				id = UUID.randomUUID().toString(),
				label = wordAsString.uppercase()
			)
			
			// Remove letters from hand, increment round, add word to words played
			// NOTE: Do NOT add wordEffect to effects yet - it will be added after animations
			val gameWithoutLetters = game.copy(
				currentRound = game.currentRound.copy(
					hand = game.currentRound.hand.filterNot { letters.contains(it) },
					round = game.currentRound.round + 1,
					wordsPlayed = game.currentRound.wordsPlayed + wordAsString,
					// TODO enemy health in the enemy update
				)
			)

			// Draw new hand
			val gameWithNewHand = drawHandUseCase(
				drawnAmount = letters.size,
				game = gameWithoutLetters
			)

			// Save game state (matching original behavior from runGameChecks)
			val savedGame = saveGameStateUseCase(gameWithNewHand) as ActiveGameState

			// Calculate scores
			val letterScores = scoreWordMapper.map(
				ScoreWordMapper.Param(
					letters = letters,
					activeGameValues = savedGame.activeGameValues
				),
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
			println("played word: invalid")
			Result.failure(
				exception = GameplayExceptions.InvalidWord(
					word = wordAsString
				)
			)
		}
	}
}
