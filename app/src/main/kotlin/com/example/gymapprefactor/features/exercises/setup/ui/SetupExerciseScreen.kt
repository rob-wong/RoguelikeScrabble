package com.example.gymapprefactor.features.exercises.setup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.common.components.ui.RectangleButtonRouter
import com.example.gymapprefactor.common.components.ui.TopBarRouter
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseState
import com.example.gymapprefactor.features.exercises.setup.presentation.viewmodel.SetupExerciseViewModelImpl

@Composable
fun SetupExerciseScreenRouter(
    modifier: Modifier = Modifier,
    viewModel: SetupExerciseViewModelImpl = hiltViewModel()
) {
    val screenState: SetupExerciseState by viewModel.state.collectAsStateWithLifecycle(
        SetupExerciseState.None
    )

    when(val state = screenState) {
        is SetupExerciseState.Content -> SetupExerciseContent(state, modifier)
        is SetupExerciseState.None -> Unit
    }
}

@Composable
fun SetupExerciseContent(
    state: SetupExerciseState.Content,
    modifier: Modifier = Modifier,
) {
    TopBarRouter(state = state.topBarState)

    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = DeviceUtil.getColumnWidthDp(1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = state.exercise.name)

        Spacer(modifier = Modifier.height(DeviceUtil.getColumnWidthDp(1)))
        RectangleButtonRouter(state = state.onSave)
    }
}
