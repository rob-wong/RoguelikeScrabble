package com.example.gymapprefactor.features.game.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.gameplayLoop.domain.ApplyScoreToEnemyUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.CheckGameConditionsUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayBusinessMediator
import com.example.gymapprefactor.business.gameplayLoop.domain.ScoredWordResult
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.presentation.models.BackgroundAction
import com.example.gymapprefactor.common.components.presentation.state.BackgroundReducer
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.state.DialogReducer
import com.example.gymapprefactor.features.game.presentation.models.GameScreenAction
import com.example.gymapprefactor.features.game.presentation.state.GameScreenReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModelImpl @Inject constructor(
	private val dialogReducer: DialogReducer,
	private val gameScreenReducer: GameScreenReducer,
	private val gameplayBusinessMediator: GameplayBusinessMediator,
	private val navigationReducer: NavigationReducer,
	private val backgroundReducer: BackgroundReducer,
	private val dispatcherProvider: DispatcherProvider,
	private val applyScoreToEnemyUseCase: ApplyScoreToEnemyUseCase,
	private val checkGameConditionsUseCase: CheckGameConditionsUseCase,
) : GameViewModel() {
	override val state = gameScreenReducer.state

	private lateinit var activeGameState: ActiveGameState
	val invalidWordEvent = MutableSharedFlow<Unit>()
	val scoreEvent = MutableSharedFlow<ScoreAnimationPayload>()
	val scoreAnimationComplete = MutableSharedFlow<Unit>()
	private val mutex = Mutex()

	init {
		setBackground()
		initGame()
	}

	private fun setBackground() {
		viewModelScope.launch(dispatcherProvider.main) {
			backgroundReducer.update(BackgroundAction.SetBackground(ScreenBackgroundState.Game))
		}
	}

	private fun initGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			activeGameState = gameplayBusinessMediator.fetchOrCreateActiveGame()
			updateGame()
		}
	}

	private suspend fun updateGame() {
		println("GameViewModelImpl.updateGame: gameLost=${activeGameState.activeGameVariables.gameLost}")
		if (activeGameState.activeGameVariables.gameLost) {
			println("GameViewModelImpl.updateGame: Triggering game lost dialog")
			triggerGameLostDialog()
		} else {
			gameScreenReducer.update(GameScreenAction.StartPlaying(
				runesCount = 10,
				glyphCount = 30,
				onQuitPressed = ::onQuitPressed,
				onWordPlayed = ::onWordPlayed,
				onDiscardPressed = ::onDiscardPressed,
				hand = activeGameState.currentRound.hand,
				currentLettersInDeck = activeGameState.currentRound.mutableDeck.size(),
				maxLettersInDeck = activeGameState.activeGameValues.deck.size(),
				discardsRemaining = activeGameState.activeGameVariables.maxDiscards - activeGameState.currentRound.discardsUsed,
				currentRound = activeGameState.currentRound.round,
				maxRounds = activeGameState.activeGameVariables.maxRounds,
			))
		}
	}

	private fun onQuitPressed() {
		viewModelScope.launch(dispatcherProvider.main) {
			dialogReducer.update(
				DialogAction.TriggerDialog(
					onDismiss = { dialogReducer.onDefaultDismiss() },
					title = "Are you sure you want to quit? ",
					confirmState = DialogAction.ConfirmState.Content(
						onConfirm = ::quitGame
					),
					showDismissButton = true
				)
			)
		}
	}

	private fun onWordPlayed(letterIds: List<String>) {
		viewModelScope.launch(dispatcherProvider.default) {
			mutex.withLock {
				gameplayBusinessMediator.onWordPlayed(
					list = letterIds.map { letterId ->
						activeGameState.currentRound.hand.first { it.id == letterId }
					},
					game = activeGameState
				).fold(
					onSuccess = { result ->
						handleScoredWord(result)
					},
					onFailure = {
						invalidWordEvent.emit(Unit)
					}
				)
			}
		}
	}

	private fun onDiscardPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			activeGameState = gameplayBusinessMediator.discardHand(activeGameState)
			updateGame()
		}
	}

	private suspend fun handleScoredWord(result: ScoredWordResult) {
		println("GameViewModelImpl.handleScoredWord -> scores=${result.letterScores}")
		activeGameState = result.gameState
		val totalScore = result.letterScores.sumOf { it.second }
		
		scoreEvent.emit(
			ScoreAnimationPayload(
				letterScores = result.letterScores,
				letters = result.letters
			)
		)
		// Wait for animation to complete before applying score and checking win/loss
		scoreAnimationComplete.first()

		activeGameState = applyScoreToEnemyUseCase(totalScore, activeGameState)

		val conditionResult = checkGameConditionsUseCase(activeGameState)
		
		println("GameViewModelImpl: After score applied - " +
				"enemyHealth=${activeGameState.currentRound.enemyHealth}, " +
				"round=${activeGameState.currentRound.round}, " +
				"maxRounds=${activeGameState.activeGameVariables.maxRounds}, " +
				"isWon=${conditionResult.isWon}, " +
				"isLost=${conditionResult.isLost}"
		)
		
		activeGameState = conditionResult.updatedGame
		
		println("GameViewModelImpl: gameLost flag set to ${activeGameState.activeGameVariables.gameLost}")
		
		// Save updated state with applied score and win/loss check
		activeGameState = gameplayBusinessMediator.saveGameState(activeGameState)
		println("GameViewModelImpl: After save - gameLost=${activeGameState.activeGameVariables.gameLost}")
		
		updateGame()
	}

	private fun quitGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			gameplayBusinessMediator.endGame(game = activeGameState, saveProgression = false)
			navigationReducer.update(NavigationAction.GoTo(NavigationPage.HomeScreen))
		}
	}

	private suspend fun triggerGameLostDialog() {
		println("GameViewModelImpl.triggerGameLostDialog: Called")
		withContext(dispatcherProvider.main) {
			println("GameViewModelImpl.triggerGameLostDialog: Updating dialog reducer on main thread")
			dialogReducer.update(
				DialogAction.TriggerDialog(
					onDismiss = { dialogReducer.onDefaultDismiss() },
					title = "Game Lost",
					showDismissButton = false,
					confirmState = DialogAction.ConfirmState.Content(
						onConfirm = { quitGame() }
					),
				))
			println("GameViewModelImpl.triggerGameLostDialog: Dialog reducer updated")
		}
	}
}
