package com.cypherose.common.components.presentation.models

import com.cypherose.business.interfaces.Action
import com.cypherose.common.components.presentation.ScreenBackgroundState

sealed class BackgroundAction : Action {
    data class SetBackground(val background: ScreenBackgroundState) : BackgroundAction()
    data object None : BackgroundAction()
}
