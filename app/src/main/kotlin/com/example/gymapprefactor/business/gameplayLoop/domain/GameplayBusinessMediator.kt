package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.business.models.GameState
import com.example.gymapprefactor.business.models.Letter

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
) {
	suspend fun fetchOrCreateActiveGame(): ActiveGameState {
		return getGameState() as? ActiveGameState ?: createGameUseCase()
	}

	suspend fun getGameState(): GameState {
		return getGameStateUseCase()
	}

	fun drawHand(drawnAmount: Int, game: ActiveGameState): ActiveGameState {
		return drawHandUseCase(drawnAmount, game)
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
		
		gameState = when {
			gameConditions.isWon -> handleWinCondition(gameState)
			gameConditions.isLost -> handleLossCondition(gameState)
			else -> gameState
		}

		return ProcessedWordResult(
			gameState = gameState,
			rawScore = rawScore,
			finalScore = finalScore,
			effectModifications = effectModifications,
			isWon = gameConditions.isWon,
			isLost = gameConditions.isLost
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

	private suspend fun handleWinCondition(gameState: ActiveGameState): ActiveGameState {
		val needsSelection = gameState.currentRound.effects.isNotEmpty()
		val gameWithWinFlag = gameState.copy(
			activeGameVariables = gameState.activeGameVariables.copy(
				gameLost = false,
				needsEffectSelection = needsSelection
			)
		)
		return saveGameState(gameWithWinFlag)
	}

	suspend fun selectEffectAndAdvance(effect: Effect, game: ActiveGameState): ActiveGameState {
		val gameWithEffect = addEffectToActiveGameValuesUseCase(effect, game)
		val gameWithFlagCleared = gameWithEffect.copy(
			activeGameVariables = gameWithEffect.activeGameVariables.copy(
				needsEffectSelection = false
			)
		)
		val savedGame = saveGameState(gameWithFlagCleared)
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
	val isLost: Boolean
)

data class ScoredWordResult(
	val gameState: ActiveGameState,
	val letterScores: List<LetterScore>,
	val letters: List<Letter>,
	val wordEffect: Effect
)
