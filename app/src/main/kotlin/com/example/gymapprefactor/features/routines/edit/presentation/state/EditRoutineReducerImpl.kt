package com.example.gymapprefactor.features.routines.edit.presentation.state

import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class EditRoutineReducerImpl @Inject constructor(
    private val rectangleButtonMapper: RectangleButtonMapper,
) : EditRoutineReducer {
    override val state = MutableStateFlow<EditRoutineState>(EditRoutineState.None)

    override suspend fun update(action: EditRoutineAction) {
        when(action) {
            is EditRoutineAction.SetContent -> setContent(action)
            is EditRoutineAction.None -> Unit
        }
    }

    private fun setContent(action: EditRoutineAction.SetContent) {
        println(state.value)
        state.value = with(action) {
            EditRoutineState.Content(
                exerciseList = exerciseSetupList,
                saveButton = rectangleButtonMapper.map(
                    RectangleButtonMapper.Param(
                        onClick = onSave,
                        text = "Save"
                    )
                ),
                nameInputField = nameInputField,
                addExerciseButton = rectangleButtonMapper.map(
                    RectangleButtonMapper.Param(
                        onClick = onClickAddExercise,
                        text = "Add Exercise"
                    )
                ),
                topBarState = topBarState,
            )
        }
        println(state.value)
    }
}
