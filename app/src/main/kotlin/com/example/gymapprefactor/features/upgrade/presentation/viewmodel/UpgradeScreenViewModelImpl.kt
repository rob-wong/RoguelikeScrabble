package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.models.User
import com.example.gymapprefactor.business.models.copy
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.upgrade.domain.UpgradeCostMapper
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeAnimationPayload
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenAction
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeLetterStateMapper
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UpgradeScreenViewModelImpl @Inject constructor(
	private val userBusinessMediator: UserBusinessMediator,
	private val upgradeLetterStateMapper: UpgradeLetterStateMapper,
	private val upgradeCostMapper: UpgradeCostMapper,
	private val upgradeScreenReducer: UpgradeScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val dispatcherProvider: DispatcherProvider,
): UpgradeScreenViewModel() {
	private var currentDeck: Deck? = null

	override val state = upgradeScreenReducer.state

	init {
		startObservingUserUpdates()
		loadInitialUserContent()
	}

	private fun startObservingUserUpdates() {
		viewModelScope.launch(dispatcherProvider.main) {
			userBusinessMediator.getUserFlow().collect { user ->
				handleUserUpdate(user)
			}
		}
	}

	private suspend fun handleUserUpdate(user: User?) {
		if (user != null) {
			updateRunesCount(user.runesCount)
		}
	}

	private fun loadInitialUserContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			userBusinessMediator.getUser().fold(
				onSuccess = { user ->
					setContent(user)
				},
				onFailure = { error ->
					Timber.e(error, "Failed to load user for upgrade screen")
				}
			)
		}
	}

	private suspend fun setContent(user: User) {
		upgradeScreenReducer.update(
			UpgradeScreenAction.SetContent(
				runesCount = user.runesCount,
				onBackPressed = ::onBackPressed,
				upgradeLetters = getUpgradeLetterStates(user.runesCount),
			)
		)
	}

	private suspend fun updateRunesCount(runesCount: Int) {
		val currentState = state.first()
		if (currentState is UpgradeScreenState.Content) {
			if (currentState.upgradeAnimation != null) {
				return
			}
			upgradeScreenReducer.update(
				UpgradeScreenAction.SetContent(
					runesCount = runesCount,
					onBackPressed = ::onBackPressed,
					upgradeLetters = getUpgradeLetterStates(runesCount),
				)
			)
		}
	}

	private suspend fun getUpgradeLetterStates(runesCount: Int): List<UpgradeLetterState> {
		val decksResult = userBusinessMediator.getDecks()
		val deck = decksResult.getOrNull()?.firstOrNull() ?: return emptyList()
		currentDeck = deck

		return deck.letters.map { letter ->
			upgradeLetterStateMapper.map(
				UpgradeLetterStateMapper.Param(
					deckType = DeckType.Default,
					letter = letter,
					runesCount = runesCount,
					upgradeCostMapper = upgradeCostMapper,
					onLetterClick = { cost -> onLetterClick(letter, cost) }
				)
			)
		}
	}

	private fun onLetterClick(letter: Letter, cost: Int) {
		viewModelScope.launch(dispatcherProvider.default) {
			val userResult = userBusinessMediator.getUser()
			val runesCount = userResult.getOrNull()?.runesCount ?: 0
			val canAfford = runesCount >= cost && letter.level < UpgradeCostMapper.MAX_LETTER_LEVEL
			
			upgradeScreenReducer.update(
				UpgradeScreenAction.SelectLetter(
					letter = letter,
					cost = cost,
					canAfford = canAfford,
					onUpgrade = { onUpgradeConfirm(letter, cost) },
					onDismiss = { onDismissSelection() }
				)
			)
		}
	}

	fun onUpgradeConfirm(letter: Letter, cost: Int) {
		viewModelScope.launch(dispatcherProvider.default) {
			val deck = currentDeck ?: return@launch
			val originalLetter = letter
			
			upgradeScreenReducer.update(UpgradeScreenAction.DismissLetterSelection)
			
			userBusinessMediator.upgradeLetter(deck = deck, letter = letter, cost = cost).fold(
				onSuccess = {
					val upgradedLetter = originalLetter.copy(level = originalLetter.level + 1)
					upgradeScreenReducer.update(
						UpgradeScreenAction.ShowUpgradeAnimation(
							UpgradeAnimationPayload(
								originalLetter = originalLetter,
								upgradedLetter = upgradedLetter
							)
						)
					)
				},
				onFailure = { error ->
					Timber.e(error, "Failed to upgrade letter")
				}
			)
		}
	}

	fun onAnimationComplete() {
		viewModelScope.launch(dispatcherProvider.default) {
			upgradeScreenReducer.update(UpgradeScreenAction.ClearUpgradeAnimation)
			userBusinessMediator.getUser().fold(
				onSuccess = { user ->
					setContent(user)
				},
				onFailure = { error ->
					Timber.e(error, "Failed to get user after upgrade")
				}
			)
		}
	}

	fun onDismissSelection() {
		viewModelScope.launch(dispatcherProvider.default) {
			upgradeScreenReducer.update(UpgradeScreenAction.DismissLetterSelection)
		}
	}

	private fun onBackPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			navigationReducer.update(NavigationAction.GoBack)
		}
	}
}
