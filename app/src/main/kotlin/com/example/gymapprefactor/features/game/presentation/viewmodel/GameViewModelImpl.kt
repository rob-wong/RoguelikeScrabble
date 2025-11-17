package com.example.gymapprefactor.features.game.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.effects.domain.GetEffectDescriptorsUseCase
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayBusinessMediator
import com.example.gymapprefactor.business.gameplayLoop.domain.ScoredWordResult
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.DiscardsRemainingMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EffectAnimationPayloadMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyCreationMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyLabelMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.MidshopOptionMapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.features.game.presentation.models.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.MidshopResultPayload
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.state.DialogReducer
import com.example.gymapprefactor.features.game.presentation.models.animation.EffectAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.GameScreenAction
import com.example.gymapprefactor.features.game.presentation.models.animation.GlyphAnimationPayload
import com.example.gymapprefactor.features.game.presentation.models.animation.ScoreAnimationPayload
import com.example.gymapprefactor.features.game.presentation.state.GameScreenReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
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

	private lateinit var activeGameState: ActiveGameState
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
		val enemyMaxHealth = enemyCreationMapper.map(
			EnemyCreationMapper.Param(
				stage = activeGameState.activeGameVariables.stage,
				level = activeGameState.activeGameVariables.level
			)
		)
		val enemyLabel = enemyLabelMapper.map(
			EnemyLabelMapper.Param(level = activeGameState.activeGameVariables.level)
		)
		val discardsRemaining = discardsRemainingMapper.map(
			DiscardsRemainingMapper.Param(game = activeGameState)
		)
		val effectDescriptors = getEffectDescriptorsUseCase()
		
		val action = createStartPlayingAction(
			enemyMaxHealth = enemyMaxHealth,
			enemyLabel = enemyLabel,
			discardsRemaining = discardsRemaining,
			effectDescriptors = effectDescriptors
		)
		gameScreenReducer.update(action)
		
		if (activeGameState.activeGameVariables.gameLost) {
			delay(300)
			triggerGameLostDialog()
		}
	}

	@SuppressWarnings("LongMethod")
	private fun createStartPlayingAction(
		enemyMaxHealth: Int,
		enemyLabel: String,
		discardsRemaining: Int,
		effectDescriptors: Map<String, EffectDescriptor>
	): GameScreenAction.StartPlaying {
		return GameScreenAction.StartPlaying(
			runesCount = 10,
			glyphCount = activeGameState.activeGameVariables.glyphCount,
			onQuitPressed = ::onQuitPressed,
			onWordPlayed = ::onWordPlayed,
			onDiscardPressed = ::onDiscardPressed,
			hand = activeGameState.currentRound.hand,
			currentLettersInDeck = activeGameState.currentRound.mutableDeck.size(),
			maxLettersInDeck = activeGameState.activeGameValues.deck.size(),
			discardsRemaining = discardsRemaining,
			currentRound = activeGameState.currentRound.round,
			maxRounds = activeGameState.activeGameVariables.maxRounds,
			enemyHealth = activeGameState.currentRound.enemyHealth,
			enemyMaxHealth = enemyMaxHealth,
			enemyLabel = enemyLabel,
			activeGameEffects = activeGameState.activeGameValues.effects,
			currentRoundEffects = activeGameState.currentRound.effects,
			effectDescriptors = effectDescriptors,
			needsEffectSelection = activeGameState.activeGameVariables.needsEffectSelection,
			effectSelectionEffects = if (activeGameState.activeGameVariables.needsEffectSelection) {
				activeGameState.currentRound.effects
			} else {
				emptyList()
			},
			onEffectSelected = if (activeGameState.activeGameVariables.needsEffectSelection) {
				::onEffectSelected
			} else {
				null
			},
			onEffectSelectionBackPressed = if (activeGameState.activeGameVariables.needsEffectSelection) {
				::quitGame
			} else {
				null
			},
			needsMidshopSelection = activeGameState.activeGameVariables.needsMidshopSelection,
			midshopOptions = if (activeGameState.activeGameVariables.needsMidshopSelection) {
				midshopOptionMapper.map(
					MidshopOptionMapper.Param(game = activeGameState)
				)
			} else {
				emptyList()
			},
			selectedMidshopOption = if (activeGameState.activeGameVariables.needsMidshopSelection) {
				selectedMidshopOption
			} else {
				null
			},
			midshopConfirmButton = IconButtonState.None, // Will be mapped in reducer
			onMidshopOptionSelected = if (activeGameState.activeGameVariables.needsMidshopSelection) {
				::onMidshopOptionSelected
			} else {
				null
			},
			onMidshopConfirmed = if (activeGameState.activeGameVariables.needsMidshopSelection) {
				::onMidshopConfirmed
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
			if (processedResult.glyphReward > 0) {
				glyphAnimationEvent.emit(
					GlyphAnimationPayload(
						amount = processedResult.glyphReward
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
			selectedEffect = effect
			activeGameState = gameplayBusinessMediator.selectEffect(
				game = activeGameState
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
			val option = selectedMidshopOption
			if (option != null) {
				val result = gameplayBusinessMediator.selectMidshopOptionAndAdvance(
					midshopOption = option,
					selectedEffect = selectedEffect,
					game = activeGameState
				)
				activeGameState = result.gameState
				selectedMidshopOption = null
				selectedEffect = null
				
				// Emit result payload if present
				if (result.resultPayload != null) {
					midshopResultEvent.emit(result.resultPayload)
					midshopResultAnimationComplete.first()
				}
				
				advanceToNextEnemy()
			}
		}
	}

	private suspend fun advanceToNextEnemy() {
		updateGame()
	}

	private fun quitGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			gameplayBusinessMediator.endGame(game = activeGameState, saveProgression = false)
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
		withContext(dispatcherProvider.main) {
			dialogReducer.update(
				DialogAction.TriggerDialog(
					onDismiss = { dialogReducer.onDefaultDismiss() },
					title = "Game Lost",
					showDismissButton = false,
					confirmState = DialogAction.ConfirmState.Content(
						onConfirm = { quitGame() }
					),
				))
		}
	}
}
