package com.example.gymapprefactor.features.exercises.events

import com.example.gymapprefactor.business.interfaces.Reducer
import kotlinx.coroutines.flow.MutableStateFlow

interface ExerciseEventReducer : Reducer<ExerciseEventState, ExerciseEventAction>

class ExerciseEventReducerImpl : ExerciseEventReducer {
    override val state = MutableStateFlow<ExerciseEventState>(ExerciseEventState.None)

    override suspend fun update(action: ExerciseEventAction) {
        when(action) {
            is ExerciseEventAction.PickExercise -> goToPickExercise(action)
            is ExerciseEventAction.SetupExercise -> goToSetupExercise(action)
            is ExerciseEventAction.None -> Unit
        }
    }

    private fun goToPickExercise(action: ExerciseEventAction.PickExercise) {
        state.value = ExerciseEventState.PickExercise(
            action.endPage,
        )
    }

    private fun goToSetupExercise(action: ExerciseEventAction.SetupExercise) {
        state.value = ExerciseEventState.SetupExercise(
            exercise = action.exercise,
            setupEndPage = action.setupEndPage,
        )
    }
}
