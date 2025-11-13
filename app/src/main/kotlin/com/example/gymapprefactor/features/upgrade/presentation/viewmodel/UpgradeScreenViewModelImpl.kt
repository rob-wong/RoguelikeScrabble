package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.presentation.models.BackgroundAction
import com.example.gymapprefactor.common.components.presentation.state.BackgroundReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenAction
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeLetterStateMapper
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpgradeScreenViewModelImpl @Inject constructor(
	private val userBusinessMediator: UserBusinessMediator,
	private val upgradeLetterStateMapper: UpgradeLetterStateMapper,
	private val upgradeScreenReducer: UpgradeScreenReducer,
	private val navigationReducer: NavigationReducer,
	private val backgroundReducer: BackgroundReducer,
	private val dispatcherProvider: DispatcherProvider,
): UpgradeScreenViewModel() {
	private lateinit var currentDeck : Deck

	override val state = upgradeScreenReducer.state

	init {
		setBackground()
		setContent()
	}

	private fun setBackground() {
		viewModelScope.launch(dispatcherProvider.main) {
			backgroundReducer.update(BackgroundAction.SetBackground(ScreenBackgroundState.Upgrade))
		}
	}

	private fun setContent() {
		viewModelScope.launch(dispatcherProvider.default) {
			upgradeScreenReducer.update(
				UpgradeScreenAction.SetContent(
					runesCount = 10,
					onBackPressed = ::onBackPressed,
					upgradeLetters = getUpgradeLetterStates(),
				)
			)
		}
	}

	private suspend fun getUpgradeLetterStates(): List<UpgradeLetterState> {
		currentDeck = userBusinessMediator.getDecks()
			.first() // temporary, while only one deck is supported

		return currentDeck.letters.map {
			upgradeLetterStateMapper.map(
				UpgradeLetterStateMapper.Param(
					deckType = DeckType.Default, // again, temporary
					letter = it,
					onUpgrade = ::onUpgradeLetter
				)
			)
		}
	}

	private fun onUpgradeLetter(letter: Letter) {
		viewModelScope.launch(dispatcherProvider.default) {
			userBusinessMediator.upgradeLetter(deck = currentDeck, letter = letter)
			setContent()
		}
	}

	private fun onBackPressed() {
		viewModelScope.launch(dispatcherProvider.default) {
			navigationReducer.update(NavigationAction.GoBack)
		}
	}
}
