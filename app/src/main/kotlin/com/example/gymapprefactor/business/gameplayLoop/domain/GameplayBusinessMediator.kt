package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EffectScoreMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EffectScoreModification
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.GlyphRewardMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.LetterScore
import com.example.gymapprefactor.business.gameplayLoop.domain.models.MidshopOptionResult
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.AddEffectToActiveGameValuesUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.AdvanceToNextEnemyUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.ApplyScoreToEnemyUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.CreateGameUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.DrawHandUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.EndGameUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.GetGameStateUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.PlayWordUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.usecases.SaveGameStateUseCase
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.business.models.GameState
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption

class GameplayBusinessMediator(
	private val getGameStateUseCase: GetGameStateUseCase,
	private val saveGameStateUseCase: SaveGameStateUseCase,
	private val endGameUseCase: EndGameUseCase,
	private val playWordUseCase: PlayWordUseCase,
	private val drawHandUseCase: DrawHandUseCase,
	private val createGameUseCase: CreateGameUseCase,
	private val applyScoreToEnemyUseCase: ApplyScoreToEnemyUseCase,
	private val effectScoreMapper: EffectScoreMapper,
	private val gameRules: GameRules,
	private val advanceToNextEnemyUseCase: AdvanceToNextEnemyUseCase,
	private val addEffectToActiveGameValuesUseCase: AddEffectToActiveGameValuesUseCase,
	private val glyphRewardMapper: GlyphRewardMapper,
	private val midshopBusinessMediator: MidshopBusinessMediator,
) {
	suspend fun fetchOrCreateActiveGame(): ActiveGameState {
		return getGameState() as? ActiveGameState ?: createGameUseCase()
	}

	suspend fun getGameState(): GameState {
		return getGameStateUseCase()
	}

	suspend fun onWordPlayed(list: List<Letter>, game: ActiveGameState): Result<ScoredWordResult> {
		return playWordUseCase(list, game)
	}

	suspend fun discardHand(game: ActiveGameState): ActiveGameState {
		val newGameState = drawHandUseCase(
			drawnAmount = game.activeGameVariables.handSize,
			game = game.copy(
				currentRound = game.currentRound.copy(
					discardsUsed = game.currentRound.discardsUsed + 1,
					hand = emptyList()
				)
			)
		)
		saveGameStateUseCase(newGameState).also { return newGameState }
	}

	suspend fun saveGameState(game: ActiveGameState): ActiveGameState {
		return saveGameStateUseCase(game) as ActiveGameState
	}

	suspend fun endGame(game: ActiveGameState, saveProgression: Boolean) {
		endGameUseCase(game, saveProgression)
	}

	suspend fun processScoredWord(
		result: ScoredWordResult
	): ProcessedWordResult {
		val rawScore = calculateRawScore(result.letterScores)
		val effectModifications = calculateEffectModifications(result.gameState, rawScore)
		val finalScore = calculateFinalScore(rawScore, effectModifications)
		
		var gameState = applyScoreAndWordEffect(result.gameState, finalScore, result.wordEffect)
		gameState = saveGameState(gameState)
		
		val gameConditions = checkGameConditions(gameState)
		
		val glyphReward = if (gameConditions.isWon) {
			val (updatedGameState, reward) = handleWinCondition(gameState)
			gameState = updatedGameState
			reward
		} else if (gameConditions.isLost) {
			gameState = handleLossCondition(gameState)
			0
		} else {
			0
		}

		return ProcessedWordResult(
			gameState = gameState,
			rawScore = rawScore,
			finalScore = finalScore,
			effectModifications = effectModifications,
			isWon = gameConditions.isWon,
			isLost = gameConditions.isLost,
			glyphReward = glyphReward
		)
	}

	private fun calculateRawScore(letterScores: List<LetterScore>): Int {
		return letterScores.sumOf { it.second }
	}

	private suspend fun calculateEffectModifications(
		gameState: ActiveGameState,
		rawScore: Int
	): List<EffectScoreModification> {
		val combinedEffects = gameState.activeGameValues.effects +
			gameState.currentRound.effects
		return effectScoreMapper.map(
			EffectScoreMapper.Param(
				effects = combinedEffects,
				rawScore = rawScore
			)
		)
	}

	private fun calculateFinalScore(
		rawScore: Int,
		effectModifications: List<EffectScoreModification>
	): Int {
		return rawScore + effectModifications.sumOf { it.scoreDelta }
	}

	private fun applyScoreAndWordEffect(
		gameState: ActiveGameState,
		finalScore: Int,
		wordEffect: Effect
	): ActiveGameState {
		val gameWithScoreApplied = applyScoreToEnemyUseCase(finalScore, gameState)
		return gameWithScoreApplied.copy(
			currentRound = gameWithScoreApplied.currentRound.copy(
				effects = gameWithScoreApplied.currentRound.effects + wordEffect
			)
		)
	}

	private fun checkGameConditions(gameState: ActiveGameState): GameConditions {
		val isWon = gameRules.checkWinCondition(gameState)
		val isLost = gameRules.checkLossCondition(gameState) && !isWon
		return GameConditions(isWon, isLost)
	}

	private suspend fun handleWinCondition(gameState: ActiveGameState): Pair<ActiveGameState, Int> {
		val needsSelection = gameState.currentRound.effects.isNotEmpty()
		val glyphReward = calculateGlyphReward(gameState)
		val gameWithRewards = applyGlyphReward(gameState, glyphReward)
		val gameWithWinFlag = gameWithRewards.copy(
			activeGameVariables = gameWithRewards.activeGameVariables.copy(
				gameLost = false,
				needsEffectSelection = needsSelection,
				needsMidshopSelection = !needsSelection // Show midshop if no effect selection needed
			)
		)
		return Pair(saveGameState(gameWithWinFlag), glyphReward)
	}

	private fun calculateGlyphReward(gameState: ActiveGameState): Int {
		return glyphRewardMapper.map(GlyphRewardMapper.Param(game = gameState))
	}

	private fun applyGlyphReward(gameState: ActiveGameState, reward: Int): ActiveGameState {
		val currentGlyphCount = gameState.activeGameVariables.glyphCount
		return gameState.copy(
			activeGameVariables = gameState.activeGameVariables.copy(
				glyphCount = currentGlyphCount + reward
			)
		)
	}

	suspend fun selectEffect(game: ActiveGameState): ActiveGameState {
		// Don't add the effect yet - just clear the selection flag and show midshop
		// The effect will be added when midshop is confirmed
		val gameWithFlagCleared = game.copy(
			activeGameVariables = game.activeGameVariables.copy(
				needsEffectSelection = false,
				needsMidshopSelection = true
			)
		)
		return saveGameState(gameWithFlagCleared)
	}
	
	suspend fun selectMidshopOptionAndAdvance(
		midshopOption: MidshopOption,
		selectedEffect: Effect?,
		game: ActiveGameState
	): MidshopOptionResult {
		// First, add the selected effect to activeGameValues if one was selected
		val gameWithEffect = if (selectedEffect != null) {
			addEffectToActiveGameValuesUseCase(selectedEffect, game)
		} else {
			game
		}
		
		val currentGlyphCount = gameWithEffect.activeGameVariables.glyphCount
		val newGlyphCount = (currentGlyphCount - midshopOption.cost).coerceAtLeast(0)
		val gameWithGlyphsDeducted = gameWithEffect.copy(
			activeGameVariables = gameWithEffect.activeGameVariables.copy(
				glyphCount = newGlyphCount,
				needsMidshopSelection = false
			)
		)
		
		val executionResult = midshopBusinessMediator.executeMidshopOption(
			midshopOption = midshopOption,
			game = gameWithGlyphsDeducted
		)
		
		val savedGame = saveGameState(executionResult.gameState)
		
		val finalGame = if (executionResult.shouldAdvance) {
			val advancedGame = advanceToNextEnemyUseCase(savedGame)
			saveGameState(advancedGame)
		} else {
			savedGame
		}
		
		return MidshopOptionResult(
			gameState = finalGame,
			resultPayload = executionResult.resultPayload
		)
	}

	suspend fun confirmAwakenLetterSelection(
		selectedLetter: Letter,
		game: ActiveGameState
	): ActiveGameState {
		val gameWithLetter = midshopBusinessMediator.confirmAwakenLetterSelection(
			selectedLetter = selectedLetter,
			game = game
		)
		val savedGame = saveGameState(gameWithLetter)
		val advancedGame = advanceToNextEnemyUseCase(savedGame)
		return saveGameState(advancedGame)
	}

	suspend fun confirmExpungeLetterSelection(
		selectedLetter: Letter,
		game: ActiveGameState
	): ActiveGameState {
		val gameWithDeletedLetter = midshopBusinessMediator.confirmExpungeLetterSelection(
			selectedLetter = selectedLetter,
			game = game
		)
		val savedGame = saveGameState(gameWithDeletedLetter)
		val advancedGame = advanceToNextEnemyUseCase(savedGame)
		return saveGameState(advancedGame)
	}

	private suspend fun handleLossCondition(
		gameState: ActiveGameState,
	): ActiveGameState {
		val gameWithLossFlag = gameState.copy(
			activeGameVariables = gameState.activeGameVariables.copy(
				gameLost = true
			)
		)
		return saveGameState(gameWithLossFlag)
	}

	private data class GameConditions(
		val isWon: Boolean,
		val isLost: Boolean
	)
}

data class ProcessedWordResult(
	val gameState: ActiveGameState,
	val rawScore: Int,
	val finalScore: Int,
	val effectModifications: List<EffectScoreModification>,
	val isWon: Boolean,
	val isLost: Boolean,
	val glyphReward: Int
)

data class ScoredWordResult(
	val gameState: ActiveGameState,
	val letterScores: List<LetterScore>,
	val letters: List<Letter>,
	val wordEffect: Effect
)

