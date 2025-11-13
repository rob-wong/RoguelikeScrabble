package com.example.gymapprefactor.common.components.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.presentation.models.BackgroundAction

interface BackgroundReducer : Reducer<ScreenBackgroundState, BackgroundAction>
