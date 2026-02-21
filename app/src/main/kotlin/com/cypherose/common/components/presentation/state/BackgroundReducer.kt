package com.cypherose.common.components.presentation.state

import com.cypherose.business.interfaces.Reducer
import com.cypherose.common.components.presentation.ScreenBackgroundState
import com.cypherose.common.components.presentation.models.BackgroundAction

interface BackgroundReducer : Reducer<ScreenBackgroundState, BackgroundAction>
