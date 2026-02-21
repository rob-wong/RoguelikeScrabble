package com.cypherose.features.game.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.features.game.presentation.models.GameScreenAction
import com.cypherose.features.game.presentation.models.GameScreenState

interface GameScreenReducer : Reducer<GameScreenState, GameScreenAction>
