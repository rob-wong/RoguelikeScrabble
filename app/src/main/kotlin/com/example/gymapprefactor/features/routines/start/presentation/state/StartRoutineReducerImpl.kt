package com.example.gymapprefactor.features.routines.start.presentation.state

import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineAction
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState
import kotlinx.coroutines.flow.MutableStateFlow

class StartRoutineReducerImpl : StartRoutineReducer {
    override val state = MutableStateFlow<StartRoutineState>(StartRoutineState.None)

    override suspend fun update(action: StartRoutineAction) {
        when(action) {
            is StartRoutineAction.SetContent -> setContent(action)
            is StartRoutineAction.None -> state.value = StartRoutineState.None
        }
    }

    private fun setContent(action: StartRoutineAction.SetContent) {
        state.value = StartRoutineState.Content(
            topBarState = action.topBarState
        )
    }
}
