package com.example.gymapprefactor.features.routines.edit.presentation.models.events

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.business.models.Routine

sealed class EditRoutineEventState : State {
    data class SelectRoutine(
        val routine: Routine
    ) : EditRoutineEventState()

    data class RoutineUpdated(
        val routine: Routine,
    ) : EditRoutineEventState()

    data object CreateRoutine : EditRoutineEventState()

    data object None : EditRoutineEventState()
}
