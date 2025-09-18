package com.example.gymapprefactor.features.exercises.pick.presentation.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState.PickEndPage.*
import com.example.gymapprefactor.features.exercises.pick.presentation.mappers.PickTemplateToButtonListMapper.*

interface PickTemplateToButtonListMapper :
    Mapper<Param, List<RectangleButtonState>> {
        data class Param(
            val state: ExerciseEventState.PickExercise,
            val allExercisesInData: List<Exercise>,
            val onClick: (Exercise) -> Unit
        )
    }

class PickTemplateToButtonListMapperImpl : PickTemplateToButtonListMapper {
    override fun map(param: Param): List<RectangleButtonState> {
        return when(param.state.endPage) {
            is Routines -> mapRoutines(param)
            is Stats -> mapStats(param)
        }
    }

    private fun mapStats(param: Param): List<RectangleButtonState> {
        return param.allExercisesInData.map { mapExerciseToButton(it, param.onClick) }
    }

    private fun mapRoutines(param: Param): List<RectangleButtonState> {
        val exercisesExistingInRoutine = getExistingList((param.state.endPage as Routines).routine)
        val returnList = excludeItemsInExisting(
            exercisesExistingInRoutine = exercisesExistingInRoutine,
            allExercisesInData = param.allExercisesInData,
        )

        return returnList.map { mapExerciseToButton(it, param.onClick) }
    }

    private fun excludeItemsInExisting(
        exercisesExistingInRoutine: List<Exercise>,
        allExercisesInData: List<Exercise>,
    ): List<Exercise> {

        val returnList = mutableListOf<Exercise>()
        allExercisesInData.forEach {
            if (it !in exercisesExistingInRoutine) {
                returnList.add(it)
            }
        }

        return returnList
    }

    private fun getExistingList(routine: Routine): List<Exercise> {
        return routine.templates.map {
            it.exercise
        }
    }

    private fun mapExerciseToButton(exercise: Exercise, onClick: (Exercise) -> Unit): RectangleButtonState {
        return RectangleButtonState.Content(
            text = exercise.name,
            onClick = { onClick(exercise) }
        )
    }
}
