package com.example.gymapprefactor.features.game.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState
import kotlinx.coroutines.flow.Flow

abstract class GameViewModel : ViewModel() {
	abstract val state: Flow<GameScreenState>
}
