package com.example.gymapprefactor.features.routines.edit.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineState

interface EditRoutineReducer : Reducer<EditRoutineState, EditRoutineAction>
