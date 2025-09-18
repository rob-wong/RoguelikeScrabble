package com.example.gymapprefactor.features.exercises.events

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Exercise


sealed class ExerciseEventState : State {
    data class PickExercise(
        val endPage : PickEndPage
    ) : ExerciseEventState()

    data class SetupExercise(
        val exercise: Exercise,
        val setupEndPage: SetupEndPage
    ) : ExerciseEventState()

    data object None : ExerciseEventState()



    sealed class PickEndPage {
        data class Routines(
            val routine: Routine,
        ) : PickEndPage()

        data object Stats : PickEndPage()
    }

    sealed class SetupEndPage {
        data class Routines(
            val routine: Routine,
        ) : SetupEndPage()

        data object Begin : SetupEndPage()
    }
}
