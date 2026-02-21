package com.cypherose.features.game.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cypherose.features.game.presentation.models.GameScreenState
import kotlinx.coroutines.flow.Flow

abstract class GameViewModel : ViewModel() {
	abstract val state: Flow<GameScreenState>
}
