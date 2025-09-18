package com.example.gymapprefactor.features.routines.base.presentation.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.exercise.DefaultTemplate
import com.example.gymapprefactor.business.models.exercise.Template
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.FoldingButtonState
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.routines.base.presentation.mappers.TemplateToButtonStateMapper.*

interface TemplateToButtonStateMapper : Mapper<Param, ButtonState> {
    data class Param(
        val template: Template,
        val removeExerciseCallback: (String) -> Unit,
        val editExerciseCallback: () -> Unit,
    )
}

class TemplateToButtonStateMapperImpl : TemplateToButtonStateMapper {
    override fun map(param: Param): ButtonState {
        return when (param.template) {
            is DefaultTemplate -> mapDefaultExerciseSetup(param)
            else -> FoldingButtonState.None
        }
    }

    private fun mapDefaultExerciseSetup(param: Param): FoldingButtonState {
        return with(param) {
            FoldingButtonState.Content(
                text = template.exercise.name,
                buttons = mapFoldedButtons(param)
            )
        }
    }

    private fun mapFoldedButtons(param: Param): List<RectangleButtonState.Content>{
        return listOf(
            RectangleButtonState.Content(
                text = "Remove",
                onClick = { param.removeExerciseCallback(param.template.id) },
            ),
            RectangleButtonState.Content(
                text = "Edit",
                onClick = param.editExerciseCallback,
            )
        )
    }
}
