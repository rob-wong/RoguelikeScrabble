package com.example.gymapprefactor.features.routines.edit.presentation.state.events

import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventState
import kotlinx.coroutines.flow.MutableStateFlow

class RoutineEventReducerImpl : RoutineEventReducer {
    override val state = MutableStateFlow<EditRoutineEventState>(EditRoutineEventState.None)

    override suspend fun update(action: EditRoutineEventAction) {
        when (action) {
            is EditRoutineEventAction.SelectRoutine -> selectRoutine(action)
            is EditRoutineEventAction.CreateRoutine -> createRoutine()
            is EditRoutineEventAction.RoutineUpdated -> routineUpdated(action)
            is EditRoutineEventAction.None -> Unit
        }
    }

    private fun routineUpdated(action: EditRoutineEventAction.RoutineUpdated) {
       state.value = EditRoutineEventState.RoutineUpdated(
           action.routine
       )
    }

    private fun selectRoutine(action: EditRoutineEventAction.SelectRoutine) {
        state.value = EditRoutineEventState.SelectRoutine(
            action.routine
        )
    }

    private fun createRoutine() {
        state.value = EditRoutineEventState.CreateRoutine
    }
}
