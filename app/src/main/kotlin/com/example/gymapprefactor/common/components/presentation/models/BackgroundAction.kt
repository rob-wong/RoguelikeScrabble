package com.example.gymapprefactor.common.components.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState

sealed class BackgroundAction : Action {
    data class SetBackground(val background: ScreenBackgroundState) : BackgroundAction()
    data object None : BackgroundAction()
}
