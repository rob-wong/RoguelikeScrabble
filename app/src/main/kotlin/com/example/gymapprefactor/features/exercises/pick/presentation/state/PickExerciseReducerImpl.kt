package com.example.gymapprefactor.features.exercises.pick.presentation.state

import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseAction
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseState
import kotlinx.coroutines.flow.MutableStateFlow

class PickExerciseReducerImpl : PickExerciseReducer {
    override val state = MutableStateFlow<PickExerciseState>(PickExerciseState.None)

    override suspend fun update(action: PickExerciseAction) {
        when (action) {
            is PickExerciseAction.SetContent -> setContent(action)
            is PickExerciseAction.None -> Unit
        }
    }

    private fun setContent(action: PickExerciseAction.SetContent) {
        state.value = PickExerciseState.Content(
            exerciseList = action.exerciseList,
            topBarState = action.topBarState,
        )
    }
}
