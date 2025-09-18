package com.example.gymapprefactor.features.homeScreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.state.HomeScreenReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModelImpl @Inject constructor(
    private val homeScreenReducer: HomeScreenReducer,
    private val rectangleButtonMapper: RectangleButtonMapper,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
) : HomeScreenViewModel() {
    override val state = homeScreenReducer.state

    init {
        setContent()
    }

    private fun setContent() {
        viewModelScope.launch(dispatcherProvider.default) {
            homeScreenReducer.update(HomeScreenAction.SetContent(
                onRoutines = mapOnRoutinesButton(),
                onExercises = mapOnExercisesButton(),
                onViewStats = mapOnViewStatsButton(),
                topBarState = getTopBar(),
            ))
        }
    }

    private fun mapOnExercisesButton(): ButtonState {
        return rectangleButtonMapper.map(
            RectangleButtonMapper.Param(
                onClick = ::onExercises,
                text = "Exercises"
            )
        )
    }

    private fun onExercises() {
        println("ROBBBB: MANAGE EXERCISES")
    }

    private fun mapOnRoutinesButton(): ButtonState {
        return rectangleButtonMapper.map(
            RectangleButtonMapper.Param(
                onClick = ::onRoutines,
                text = "Routines",
            )
        )
    }

    private fun onRoutines() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.BaseRoutineScreen))
        }
    }

    private fun mapOnViewStatsButton(): ButtonState {
        return rectangleButtonMapper.map(
            RectangleButtonMapper.Param(
                onClick = ::onViewStats,
                text = "View Stats"
            )
        )
    }

    private fun onViewStats() {
        println("ROBBBB: VIEW STATS")
    }

    private fun getTopBar(): TopBarState {
        return TopBarState.Content(
            title = "Home Screen",
            onBack = ::onBack
        )
    }

    private fun onBack() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoBack)
        }
    }
}
