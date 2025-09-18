package com.example.gymapprefactor.features.routines.start.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState
import com.example.gymapprefactor.features.routines.start.presentation.viewmodel.StartRoutineViewModelImpl

@Composable
fun StartRoutineRouter(
    modifier: Modifier = Modifier,
    viewModel: StartRoutineViewModelImpl = hiltViewModel(),
) {
    val screenState: StartRoutineState by viewModel.state.collectAsStateWithLifecycle(
        StartRoutineState.None
    )
    
    when(val state = screenState) {
        is StartRoutineState.Content -> Unit
        is StartRoutineState.None -> StartRoutineContent(state = state, modifier = modifier)
    }
}

@Composable
fun StartRoutineContent(
    state: StartRoutineState,
    modifier: Modifier = Modifier,
) {
    if(state is StartRoutineState.None) {
        println("wsp g")
    }
    Column(modifier = modifier) {
//        RectangularButton(onClick = {}, text = "this is the start screen")
    }
}

@Preview
@Composable
internal fun StartRoutinePreview(

) {
    StartRoutineContent(state = StartRoutineState.None)
}
