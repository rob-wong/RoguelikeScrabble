package com.example.gymapprefactor.features.routines.base.presentation.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState

interface RoutineToButtonStateMapper :
    Mapper<RoutineToButtonStateMapper.Param, RectangleButtonState> {
    data class Param(
        val routine: Routine,
        val navigationCallback: () -> Unit,
        val editRoutineEventCallback: (Routine) -> Unit,
    )
}

class RoutineToButtonStateMapperImpl : RoutineToButtonStateMapper {
    override fun map(param: RoutineToButtonStateMapper.Param): RectangleButtonState {
        return when (param.routine) {
            is DefaultRoutine -> mapDefaultRoutine(param)
            else -> RectangleButtonState.None
        }
    }

    private fun mapDefaultRoutine(param: RoutineToButtonStateMapper.Param): RectangleButtonState {
        return with(param) {
            RectangleButtonState.Content(
                onClick = {
                    navigationCallback()
                    editRoutineEventCallback(routine)
                },
                text = routine.name,
            )
        }
    }
}
