package com.example.gymapprefactor.features.routines.base.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenAction
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState

interface BaseRoutineScreenReducer : Reducer<BaseRoutineScreenState, BaseRoutineScreenAction>
