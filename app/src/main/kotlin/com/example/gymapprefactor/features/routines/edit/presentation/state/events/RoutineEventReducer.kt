package com.example.gymapprefactor.features.routines.edit.presentation.state.events

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventState

interface RoutineEventReducer : Reducer<EditRoutineEventState, EditRoutineEventAction>
