package com.example.gymapprefactor.features.routines.start.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineAction
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState

interface StartRoutineReducer : Reducer<StartRoutineState, StartRoutineAction>
