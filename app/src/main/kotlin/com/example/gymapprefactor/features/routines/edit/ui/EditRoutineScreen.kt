package com.example.gymapprefactor.features.routines.edit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.ui.ButtonRouter
import com.example.gymapprefactor.common.components.ui.TextInputFieldRouter
import com.example.gymapprefactor.common.components.ui.TopBarRouter
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineState
import com.example.gymapprefactor.features.routines.edit.presentation.viewmodel.EditRoutineViewModelImpl
import com.example.gymapprefactor.features.routines.edit.ui.previews.EditRoutineScreenProvider

@Composable
fun EditRoutineScreenRouter(
    modifier: Modifier = Modifier,
    viewModel: EditRoutineViewModelImpl = hiltViewModel()
) {
    val screenState: EditRoutineState by viewModel.state.collectAsStateWithLifecycle(
        EditRoutineState.None
    )

    when (val state = screenState) {
        is EditRoutineState.Content -> EditRoutineScreenContent(modifier, state)
        is EditRoutineState.None -> Unit
    }
}

@Composable
private fun EditRoutineScreenContent(
    modifier: Modifier = Modifier,
    state: EditRoutineState.Content
) {
    TopBarRouter(state = state.topBarState)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = DeviceUtil.getColumnWidthDp(1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(DeviceUtil.getColumnWidthDp(2)))

        TextInputFieldRouter(state = state.nameInputField)

        Spacer(modifier = Modifier.height(DeviceUtil.getColumnWidthDp(1)))

        ExerciseButtonsRouter(state)

        ButtonRouter(state.saveButton)
    }
}

@Composable
private fun ExerciseButtonsRouter(state: EditRoutineState.Content) {
    state.exerciseList.forEach {
        ButtonRouter(it)
        Spacer(modifier = Modifier.height(SpacerUtil.spacer_06))
    }
    ButtonRouter(state.addExerciseButton)
    Spacer(modifier = Modifier.height(DeviceUtil.getColumnWidthDp(2)))
}

@Composable
@Preview
fun EditRoutineScreenPreview(
    @PreviewParameter(EditRoutineScreenProvider::class) state: EditRoutineState.Content
) {
    EditRoutineScreenContent(state = state, modifier = Modifier)
}
