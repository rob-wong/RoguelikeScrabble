package com.example.gymapprefactor.features.routines.edit.presentation.models.events

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.models.Routine

sealed class EditRoutineEventAction : Action {
    data class SelectRoutine(
        val routine: Routine,
    ) : EditRoutineEventAction()

    data class RoutineUpdated(
        val routine: Routine,
    ) : EditRoutineEventAction()

    data object CreateRoutine : EditRoutineEventAction()

    data object None : EditRoutineEventAction()
}
