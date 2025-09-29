package com.example.gymapprefactor.features.game.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.features.game.presentation.models.GameScreenAction
import com.example.gymapprefactor.features.game.presentation.state.GameScreenReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModelImpl @Inject constructor(
	private val gameScreenReducer: GameScreenReducer,
	private val dispatcherProvider: DispatcherProvider,
) : GameViewModel() {
	override val state = gameScreenReducer.state

	init {
		startGame()
	}

	private fun startGame() {
		viewModelScope.launch(dispatcherProvider.default) {
			gameScreenReducer.update(GameScreenAction.StartPlaying(
				runesCount = 10,
				glyphCount = 30,
			))
		}
	}
}
