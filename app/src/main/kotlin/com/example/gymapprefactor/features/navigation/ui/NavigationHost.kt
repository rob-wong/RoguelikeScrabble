package com.example.gymapprefactor.features.navigation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.features.exercises.pick.ui.PickExerciseRouter
import com.example.gymapprefactor.features.exercises.setup.ui.SetupExerciseScreenRouter
import com.example.gymapprefactor.features.homeScreen.ui.HomeScreenRouter
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage.*
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.viewmodel.NavigationViewModelImpl
import com.example.gymapprefactor.features.routines.base.ui.BaseRoutineScreenRouter
import com.example.gymapprefactor.features.routines.edit.ui.EditRoutineScreenRouter

@Composable
fun NavigationHost(
    viewModel: NavigationViewModelImpl = hiltViewModel(),
) {

    BackHandler {
        viewModel.onBackPressed()
    }

    val screenState: NavigationState by viewModel.state.collectAsStateWithLifecycle(
        NavigationState.None
    )

    when(val state = screenState) {
        is NavigationState.CurrentPage -> NavigationPage(state)
        is NavigationState.None -> Unit
    }
}

@Composable
fun NavigationPage(
    state: NavigationState.CurrentPage
) {
    Column {
        NavigationPageRouter(state = state)
    }
}

@Composable
fun NavigationPageRouter(
    state: NavigationState.CurrentPage
) {
    when(state.page) {
        is HomeScreen -> HomeScreenRouter()
        is BaseRoutineScreen -> BaseRoutineScreenRouter()
        is EditRoutineScreen -> EditRoutineScreenRouter()
        is PickExerciseScreen -> PickExerciseRouter()
        is SetupExerciseScreen -> SetupExerciseScreenRouter()
    }
}
