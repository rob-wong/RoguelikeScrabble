package com.cypherose.common.components.presentation.state

import com.cypherose.common.components.presentation.ScreenBackgroundState
import com.cypherose.common.components.presentation.models.BackgroundAction
import kotlinx.coroutines.flow.MutableStateFlow

class BackgroundReducerImpl : BackgroundReducer {
    override val state = MutableStateFlow<ScreenBackgroundState>(ScreenBackgroundState.None)

    override suspend fun update(action: BackgroundAction) {
        when (action) {
            is BackgroundAction.SetBackground -> setBackground(action.background)
            is BackgroundAction.None -> Unit
        }
    }

    private fun setBackground(background: ScreenBackgroundState) {
        state.value = background
    }
}
