package com.cypherose.features.game.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.business.effects.domain.GetEffectDescriptorsUseCase
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.gameplayLoop.domain.GameplayBusinessMediator
import com.cypherose.business.gameplayLoop.domain.ScoredWordResult
import com.cypherose.business.gameplayLoop.domain.mappers.DiscardsRemainingMapper
import com.cypherose.business.gameplayLoop.domain.mappers.EffectAnimationPayloadMapper
import com.cypherose.business.gameplayLoop.domain.mappers.EnemyCreationMapper
import com.cypherose.business.gameplayLoop.domain.mappers.EnemyLabelMapper
import com.cypherose.business.gameplayLoop.domain.mappers.MidshopOptionMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.Effect
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.business.models.Letter
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopResultPayload
import com.cypherose.features.game.presentation.models.midshop.MidshopLetterSelectionState
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.state.DialogReducer
import com.cypherose.features.game.presentation.models.animation.EffectAnimationPayload
import com.cypherose.features.game.presentation.models.GameScreenAction
import com.cypherose.features.game.presentation.models.animation.GlyphAnimationPayload
import com.cypherose.features.game.presentation.models.animation.ScoreAnimationPayload
import com.cypherose.features.game.presentation.state.GameScreenReducer
import com.cypherose.features.navigation.presentation.models.NavigationAction
import com.cypherose.features.navigation.presentation.models.NavigationPage
import com.cypherose.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
	private val dispatcherProvider: DispatcherProvider,
	private val enemyCreationMapper: EnemyCreationMapper,
	private val enemyLabelMapper: EnemyLabelMapper,
	private val discardsRemainingMapper: DiscardsRemainingMapper,
	private val effectAnimationPayloadMapper: EffectAnimationPayloadMapper,
	private val getEffectDescriptorsUseCase: GetEffectDescriptorsUseCase,
	private val midshopOptionMapper: MidshopOptionMapper,
) : GameViewModel() {
	override val state = gameScreenReducer.state

	private var activeGameState: ActiveGameState? = null
	val invalidWordEvent = MutableSharedFlow<Unit>()
	val scoreEvent = MutableSharedFlow<ScoreAnimationPayload>()
	val scoreAnimationComplete = MutableSharedFlow<Unit>()
	val effectAnimationEvent = MutableSharedFlow<List<EffectAnimationPayload>>()
	val effectAnimationComplete = MutableSharedFlow<Unit>()
	val levelAdvanceShakeTrigger = MutableSharedFlow<Unit>()
	val glyphAnimationEvent = MutableSharedFlow<GlyphAnimationPayload>()
	val glyphAnimationComplete = MutableSharedFlow<Unit>()
	val midshopResultEvent = MutableSharedFlow<MidshopResultPayload>()
	val midshopResultAnimationComplete = MutableSharedFlow<Unit>()
	private val mutex = Mutex()
	private var selectedMidshopOption: MidshopOption? = null
	private var selectedEffect: Effect? = null
	private var selectedAwakenLetter: Letter? = null
	private var awakenLetters: List<Letter> = emptyList()
	private var selectedExpungeLetter: Letter? = null
	private var expungeLetters: List<Letter> = emptyList()

	init {
		initGame()
	}

	private fun initGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			activeGameState = gameplayBusinessMediator.fetchOrCreateActiveGame()
			updateGame()
		}
	}

	private suspend fun updateGame() {
		val gameState = activeGameState ?: return
		val enemyMaxHealth = enemyCreationMapper.map(
			EnemyCreationMapper.Param(
				stage = gameState.activeGameVariables.stage,
				level = gameState.activeGameVariables.level
			)
		)
		val enemyLabel = enemyLabelMapper.map(
			EnemyLabelMapper.Param(level = gameState.activeGameVariables.level)
		)
		val discardsRemaining = discardsRemainingMapper.map(
			DiscardsRemainingMapper.Param(game = gameState)
		)
		val effectDescriptors = getEffectDescriptorsUseCase()
		
		val action = createStartPlayingAction(
			enemyMaxHealth = enemyMaxHealth,
			enemyLabel = enemyLabel,
			discardsRemaining = discardsRemaining,
			effectDescriptors = effectDescriptors
		)
		gameScreenReducer.update(action)
		
		if (gameState.activeGameVariables.gameLost) {
			delay(300)
			triggerGameLostDialog()
		}
	}

	@SuppressWarnings("LongMethod")
	private suspend fun createStartPlayingAction(
		enemyMaxHealth: Int,
		enemyLabel: String,
		discardsRemaining: Int,
		effectDescriptors: Map<String, EffectDescriptor>
	): GameScreenAction.StartPlaying {
		val gameState = activeGameState ?: error("activeGameState not initialized")
		val startingRunes = gameState.activeGameVariables.startingRunesCount
		val earnedRunes = gameState.activeGameVariables.runesCount - startingRunes
		val displayRunesCount = startingRunes + earnedRunes
		return GameScreenAction.StartPlaying(
			runesCount = displayRunesCount,
			glyphCount = gameState.activeGameVariables.glyphCount,
			onQuitPressed = ::onQuitPressed,
			onWordPlayed = ::onWordPlayed,
			onDiscardPressed = ::onDiscardPressed,
			hand = gameState.currentRound.hand,
			currentLettersInDeck = gameState.currentRound.mutableDeck.size(),
			maxLettersInDeck = gameState.activeGameValues.deck.size(),
			discardsRemaining = discardsRemaining,
			currentRound = gameState.currentRound.round,
			maxRounds = gameState.activeGameVariables.maxRounds,
			enemyHealth = gameState.currentRound.enemyHealth,
			enemyMaxHealth = enemyMaxHealth,
			enemyLabel = enemyLabel,
			activeGameEffects = gameState.activeGameValues.effects,
			currentRoundEffects = gameState.currentRound.effects,
			effectDescriptors = effectDescriptors,
			needsEffectSelection = gameState.activeGameVariables.needsEffectSelection,
			effectSelectionEffects = if (gameState.activeGameVariables.needsEffectSelection) {
				gameState.currentRound.effects
			} else {
				emptyList()
			},
			onEffectSelected = if (gameState.activeGameVariables.needsEffectSelection) {
				::onEffectSelected
			} else {
				null
			},
			onEffectSelectionBackPressed = if (gameState.activeGameVariables.needsEffectSelection) {
				::quitGame
			} else {
				null
			},
			needsMidshopSelection = gameState.activeGameVariables.needsMidshopSelection,
			midshopOptions = if (gameState.activeGameVariables.needsMidshopSelection) {
				midshopOptionMapper.map(
					MidshopOptionMapper.Param(game = gameState)
				)
			} else {
				emptyList()
			},
			selectedMidshopOption = if (gameState.activeGameVariables.needsMidshopSelection) {
				selectedMidshopOption
			} else {
				null
			},
			midshopConfirmButton = IconButtonState.None, // Will be mapped in reducer
			onMidshopOptionSelected = if (gameState.activeGameVariables.needsMidshopSelection) {
				::onMidshopOptionSelected
			} else {
				null
			},
			onMidshopConfirmed = if (gameState.activeGameVariables.needsMidshopSelection) {
				::onMidshopConfirmed
			} else {
				null
			},
			awakenLetterSelection = if (awakenLetters.isNotEmpty()) {
				MidshopLetterSelectionState(
					needsSelection = true,
					letters = awakenLetters,
					selectedLetter = selectedAwakenLetter,
					confirmButton = IconButtonState.None, // Will be mapped in reducer
					onLetterSelected = ::onAwakenLetterSelected,
					onConfirmed = ::onAwakenConfirmed
				)
			} else {
				null
			},
			expungeLetterSelection = if (expungeLetters.isNotEmpty()) {
				MidshopLetterSelectionState(
					needsSelection = true,
					letters = expungeLetters,
					selectedLetter = selectedExpungeLetter,
					confirmButton = IconButtonState.None, // Will be mapped in reducer
					onLetterSelected = ::onExpungeLetterSelected,
					onConfirmed = ::onExpungeConfirmed
				)
			} else {
				null
			},
		)
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
			val gameState = activeGameState ?: return@launch
			mutex.withLock {
				gameplayBusinessMediator.onWordPlayed(
					list = letterIds.map { letterId ->
						gameState.currentRound.hand.first { it.id == letterId }
					},
					game = gameState
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
			val gameState = activeGameState ?: return@launch
			activeGameState = gameplayBusinessMediator.discardHand(gameState)
			updateGame()
		}
	}

	private suspend fun handleScoredWord(result: ScoredWordResult) {
		emitScoreAnimation(result)
		
		val processedResult = gameplayBusinessMediator.processScoredWord(result)
		activeGameState = processedResult.gameState
		
		val effectAnimations = effectAnimationPayloadMapper.map(
			EffectAnimationPayloadMapper.Param(
				effectModifications = processedResult.effectModifications,
				rawScore = processedResult.rawScore
			)
		)

		if (effectAnimations.isNotEmpty()) {
			effectAnimationEvent.emit(effectAnimations)
			effectAnimationComplete.first()
		}

		if (processedResult.isWon) {
			if (processedResult.glyphReward > 0 || processedResult.runeReward > 0) {
				glyphAnimationEvent.emit(
					GlyphAnimationPayload(
						glyphAmount = processedResult.glyphReward,
						runeAmount = processedResult.runeReward
					)
				)
			}
			updateGame()
			delay(500)
			levelAdvanceShakeTrigger.emit(Unit)
			updateGame()
		} else {
			updateGame()
		}
	}

	private fun onEffectSelected(effect: Effect) {
		viewModelScope.launch(dispatcherProvider.default) {
			val gameState = activeGameState ?: return@launch
			selectedEffect = effect
			activeGameState = gameplayBusinessMediator.selectEffect(
				game = gameState
			)
			// After effect selection, show midshop instead of advancing immediately
			updateGame()
		}
	}
	
	private fun onMidshopOptionSelected(option: MidshopOption) {
		viewModelScope.launch(dispatcherProvider.default) {
			selectedMidshopOption = option
			updateGame()
		}
	}
	
	private fun onMidshopConfirmed() {
		viewModelScope.launch(dispatcherProvider.default) {
			val gameState = activeGameState ?: return@launch
			val option = selectedMidshopOption
			if (option != null) {
				val result = gameplayBusinessMediator.selectMidshopOptionAndAdvance(
					midshopOption = option,
					selectedEffect = selectedEffect,
					game = gameState
				)
				activeGameState = result.gameState

				when (result.resultPayload) {
					is MidshopResultPayload.Awaken -> {
						awakenLetters = result.resultPayload.generatedLetters
						selectedAwakenLetter = null
						selectedMidshopOption = null

						updateGame()
						delay(50)

						midshopResultEvent.emit(result.resultPayload)
					}
					is MidshopResultPayload.Expunge -> {
						expungeLetters = result.resultPayload.lettersToChooseFrom
						selectedExpungeLetter = null
						selectedMidshopOption = null

						updateGame()
						delay(50)

						midshopResultEvent.emit(result.resultPayload)
					}
					else -> {
						selectedMidshopOption = null
						selectedEffect = null

						if (result.resultPayload != null) {
							midshopResultEvent.emit(result.resultPayload)
							midshopResultAnimationComplete.first()
						}
						
						advanceToNextEnemy()
					}
				}
			}
		}
	}
	
	private fun onAwakenLetterSelected(letter: Letter) {
		viewModelScope.launch(dispatcherProvider.default) {
			selectedAwakenLetter = letter
			updateGame()
		}
	}
	
	private fun onAwakenConfirmed() {
		viewModelScope.launch(dispatcherProvider.default) {
			val gameState = activeGameState ?: return@launch
			val letter = selectedAwakenLetter
			if (letter != null) {
				// Add letter to deck and advance to next enemy
				activeGameState = gameplayBusinessMediator.confirmAwakenLetterSelection(
					selectedLetter = letter,
					game = gameState
				)
				
				// Clear state
				selectedAwakenLetter = null
				awakenLetters = emptyList()
				selectedMidshopOption = null
				selectedEffect = null
				
				// Clear the midshop result so the overlay disappears
				midshopResultAnimationComplete.emit(Unit)
				
				// Update game state (advancement already happened in confirmAwakenLetterSelection)
				advanceToNextEnemy()
			}
		}
	}

	private fun onExpungeLetterSelected(letter: Letter) {
		viewModelScope.launch(dispatcherProvider.default) {
			selectedExpungeLetter = letter
			updateGame()
		}
	}
	
	private fun onExpungeConfirmed() {
		viewModelScope.launch(dispatcherProvider.default) {
			val gameState = activeGameState ?: return@launch
			val letter = selectedExpungeLetter
			if (letter != null) {
				// Delete letter from deck and advance to next enemy
				activeGameState = gameplayBusinessMediator.confirmExpungeLetterSelection(
					selectedLetter = letter,
					game = gameState
				)
				
				// Clear state
				selectedExpungeLetter = null
				expungeLetters = emptyList()
				selectedMidshopOption = null
				selectedEffect = null
				
				// Clear the midshop result so the overlay disappears
				midshopResultAnimationComplete.emit(Unit)
				
				// Update game state (advancement already happened in confirmExpungeLetterSelection)
				advanceToNextEnemy()
			}
		}
	}

	private suspend fun advanceToNextEnemy() {
		updateGame()
	}

	private fun quitGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			val gameState = activeGameState ?: return@launch
			val shouldSaveProgression = gameState.activeGameVariables.gameLost
			gameplayBusinessMediator.endGame(game = gameState, saveProgression = shouldSaveProgression)
			navigationReducer.update(NavigationAction.GoTo(NavigationPage.HomeScreen))
		}
	}

	private suspend fun emitScoreAnimation(result: ScoredWordResult) {
		scoreEvent.emit(
			ScoreAnimationPayload(
				letterScores = result.letterScores,
				letters = result.letters
			)
		)
		scoreAnimationComplete.first()
	}

	private suspend fun triggerGameLostDialog() {
		val gameState = activeGameState ?: return
		withContext(dispatcherProvider.main) {
			val runesCount = gameState.activeGameVariables.runesCount
			val message = if (runesCount > 0) {
				"Runes earned: $runesCount"
			} else {
				null
			}
			dialogReducer.update(
				DialogAction.TriggerDialog(
					onDismiss = { dialogReducer.onDefaultDismiss() },
					title = "Game Lost",
					message = message,
					showDismissButton = false,
					confirmState = DialogAction.ConfirmState.Content(
						onConfirm = { quitGame() }
					),
				))
		}
	}
}
