package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenAction
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
	private val upgradeScreenReducer: UpgradeScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val dispatcherProvider: DispatcherProvider,
): UpgradeScreenViewModel() {
	private lateinit var currentDeck : Deck

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

	private suspend fun handleUserUpdate(user: com.example.gymapprefactor.business.models.User?) {
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

	private suspend fun setContent(user: com.example.gymapprefactor.business.models.User) {
		upgradeScreenReducer.update(
			UpgradeScreenAction.SetContent(
				runesCount = user.runesCount,
				onBackPressed = ::onBackPressed,
				upgradeLetters = getUpgradeLetterStates(),
			)
		)
	}

	private suspend fun updateRunesCount(runesCount: Int) {
		val currentState = state.first()
		if (currentState is com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState.Content) {
			upgradeScreenReducer.update(
				UpgradeScreenAction.SetContent(
					runesCount = runesCount,
					onBackPressed = ::onBackPressed,
					upgradeLetters = currentState.letters,
				)
			)
		}
	}

	private suspend fun getUpgradeLetterStates(): List<UpgradeLetterState> {
		val decksResult = userBusinessMediator.getDecks()
		currentDeck = decksResult.getOrNull()?.firstOrNull() ?: return emptyList()

		return currentDeck.letters.map {
			upgradeLetterStateMapper.map(
				UpgradeLetterStateMapper.Param(
					deckType = DeckType.Default,
					letter = it,
					onUpgrade = ::onUpgradeLetter
				)
			)
		}
	}

	private fun onUpgradeLetter(letter: Letter) {
		viewModelScope.launch(dispatcherProvider.default) {
			userBusinessMediator.upgradeLetter(deck = currentDeck, letter = letter).fold(
				onSuccess = {
					userBusinessMediator.getUser().fold(
						onSuccess = { user ->
							setContent(user)
						},
						onFailure = { error ->
							Timber.e(error, "Failed to get user after upgrade")
						}
					)
				},
				onFailure = { error ->
					Timber.e(error, "Failed to upgrade letter")
				}
			)
		}
	}

	private fun onBackPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			navigationReducer.update(NavigationAction.GoBack)
		}
	}
}
