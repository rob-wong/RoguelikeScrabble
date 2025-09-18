package com.example.gymapprefactor.features.exercises.events

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.models.exercise.Exercise

sealed class ExerciseEventAction : Action {
    data class PickExercise(
        val endPage : ExerciseEventState.PickEndPage
    ) : ExerciseEventAction()

    data class SetupExercise(
        val exercise: Exercise,
        val setupEndPage: ExerciseEventState.SetupEndPage
    ) : ExerciseEventAction()

    data object None : ExerciseEventAction()
}
