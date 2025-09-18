package com.example.gymapprefactor.features.homeScreen.presentation.state

import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class HomeScreenReducerImpl : HomeScreenReducer {
    override val state = MutableStateFlow<HomeScreenState>(HomeScreenState.None)

    override suspend fun update(action: HomeScreenAction) {
        when(action) {
            is HomeScreenAction.SetContent -> setContent(action)
            is HomeScreenAction.None -> Unit
        }
    }

    private fun setContent(action: HomeScreenAction.SetContent) {
        state.value = with(action) {
            HomeScreenState.Content(
                onRoutines = onRoutines,
                onExercises = onExercises,
                onViewStats = onViewStats,
                topBarState = topBarState,
            )
        }
    }
}
