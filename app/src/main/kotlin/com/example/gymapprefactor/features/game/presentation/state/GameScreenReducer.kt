package com.example.gymapprefactor.features.game.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.game.presentation.models.GameScreenAction
import com.example.gymapprefactor.features.game.presentation.models.GameScreenState

interface GameScreenReducer : Reducer<GameScreenState, GameScreenAction>
