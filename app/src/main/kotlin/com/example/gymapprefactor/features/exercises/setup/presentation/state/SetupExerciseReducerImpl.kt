package com.example.gymapprefactor.features.exercises.setup.presentation.state

import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseAction
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseState
import kotlinx.coroutines.flow.MutableStateFlow

class SetupExerciseReducerImpl : SetupExerciseReducer {
    override val state = MutableStateFlow<SetupExerciseState>(SetupExerciseState.None)

    override suspend fun update(action: SetupExerciseAction) {
        when (action) {
            is SetupExerciseAction.SetContent -> setContent(action)
            is SetupExerciseAction.None -> Unit
        }
    }

    private fun setContent(action: SetupExerciseAction.SetContent) {
        state.value = with(action) {
            SetupExerciseState.Content(
                exercise = exercise,
                onSave = onSave,
                topBarState = topBarState
            )
        }
    }
}
