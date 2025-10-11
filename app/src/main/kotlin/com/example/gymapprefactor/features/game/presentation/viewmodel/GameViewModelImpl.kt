package com.example.gymapprefactor.features.game.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayBusinessMediator
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogAction
import com.example.gymapprefactor.features.dialogs.presentation.state.DialogReducer
import com.example.gymapprefactor.features.game.presentation.models.GameScreenAction
import com.example.gymapprefactor.features.game.presentation.state.GameScreenReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModelImpl @Inject constructor(
	private val dialogReducer: DialogReducer,
	private val gameScreenReducer: GameScreenReducer,
	private val gameplayBusinessMediator: GameplayBusinessMediator,
	private val navigationReducer: NavigationReducer,
	private val dispatcherProvider: DispatcherProvider,
) : GameViewModel() {
	override val state = gameScreenReducer.state

	private lateinit var activeGameState: ActiveGameState

	init {
		startGame()
	}

	private fun startGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			activeGameState = gameplayBusinessMediator.fetchOrCreateActiveGame()

			gameScreenReducer.update(GameScreenAction.StartPlaying(
				runesCount = 10,
				glyphCount = 30,
				onQuitPressed = ::onQuitPressed,
				hand = activeGameState.currentRound.hand
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

	private fun quitGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			gameplayBusinessMediator.endGame(game = activeGameState, saveProgression = false)
			navigationReducer.update(NavigationAction.GoTo(NavigationPage.HomeScreen))
		}
	}
}
