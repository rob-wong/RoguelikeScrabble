package com.example.gymapprefactor.features.routines.base.presentation.state

import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenAction
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class BaseRoutineScreenReducerImpl : BaseRoutineScreenReducer {
    override val state = MutableStateFlow<BaseRoutineScreenState>(BaseRoutineScreenState.None)

    override suspend fun update(action: BaseRoutineScreenAction) {
        when(action) {
            is BaseRoutineScreenAction.SetContent -> setContent(action)
            is BaseRoutineScreenAction.None -> Unit
        }
    }

    private fun setContent(action: BaseRoutineScreenAction.SetContent) {
        state.value = BaseRoutineScreenState.Content(
            routineButtons = action.routineButtons,
            createRoutineButton = action.createRoutineButton,
            topBarState = action.topBarState,
        )
    }
}
