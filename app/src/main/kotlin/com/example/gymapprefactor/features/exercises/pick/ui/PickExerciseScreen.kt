package com.example.gymapprefactor.features.exercises.pick.ui

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
import com.example.gymapprefactor.common.components.ui.RectangleButtonRouter
import com.example.gymapprefactor.common.components.ui.TopBarRouter
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseState
import com.example.gymapprefactor.features.exercises.pick.presentation.viewmodel.PickExerciseViewModelImpl
import com.example.gymapprefactor.features.exercises.pick.ui.preview.PickExerciseScreenProvider

@Composable
fun PickExerciseRouter(
    modifier: Modifier = Modifier,
    viewModel: PickExerciseViewModelImpl = hiltViewModel()
) {
    val screenState: PickExerciseState by viewModel.state.collectAsStateWithLifecycle(
        PickExerciseState.None
    )

    when(val state = screenState) {
        is PickExerciseState.Content -> PickExerciseContent(state = state, modifier = modifier)
        is PickExerciseState.None -> Unit
    }
}

@Composable
fun PickExerciseContent(
    modifier: Modifier = Modifier,
    state: PickExerciseState.Content,
) {
    TopBarRouter(state = state.topBarState)

    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = DeviceUtil.getColumnWidthDp(1)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.exerciseList.forEach {
            RectangleButtonRouter(it)
            Spacer(modifier = Modifier.height(SpacerUtil.spacer_18))
        }
    }
}

@Preview
@Composable
internal fun PickExerciseScreenPreview(
    @PreviewParameter(PickExerciseScreenProvider::class) state: PickExerciseState.Content
) {
    PickExerciseContent(state = state)
}
