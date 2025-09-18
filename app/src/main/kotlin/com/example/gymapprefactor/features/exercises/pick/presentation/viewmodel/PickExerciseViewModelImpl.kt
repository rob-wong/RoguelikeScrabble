package com.example.gymapprefactor.features.exercises.pick.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.exercises.domain.GetExercisesUseCase
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventAction
import com.example.gymapprefactor.features.exercises.events.ExerciseEventReducer
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState.PickEndPage.*
import com.example.gymapprefactor.features.exercises.pick.presentation.mappers.PickTemplateToButtonListMapper
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseAction
import com.example.gymapprefactor.features.exercises.pick.presentation.state.PickExerciseReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickExerciseViewModelImpl @Inject constructor(
    private val exerciseEventReducer: ExerciseEventReducer,
    private val pickTemplateToButtonListMapper: PickTemplateToButtonListMapper,
    private val getExercisesUseCase: GetExercisesUseCase,
    private val pickExerciseReducer: PickExerciseReducer,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
) : PickExerciseViewModel() {
    override val state = pickExerciseReducer.state

    private lateinit var eventState: ExerciseEventState.PickExercise

    init {
        initCollection()
    }

    private fun initCollection() {
        viewModelScope.launch(dispatcherProvider.default) {
            exerciseEventReducer.state.collect {
                when (it) {
                    is ExerciseEventState.PickExercise -> setContent(it)
                    else -> Unit
                }
            }
        }
    }

    private suspend fun setContent(state: ExerciseEventState.PickExercise) {
        eventState = state
        pickExerciseReducer.update(mapAction(state))
    }

    private suspend fun mapAction(eventState: ExerciseEventState.PickExercise): PickExerciseAction {
        return PickExerciseAction.SetContent(
            exerciseList = pickTemplateToButtonListMapper.map(
                PickTemplateToButtonListMapper.Param(
                    state = eventState,
                    allExercisesInData = getExercisesUseCase(),
                    onClick = onClick()
                )
            ),
            topBarState = getTopBar()
        )
    }

    private fun getTopBar(): TopBarState {
        return TopBarState.Content(
            title = "Pick Exercise",
            onBack = ::onBack
        )
    }

    private fun onBack() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoBack)
        }
    }

    private fun onClick(): (Exercise) -> Unit {
        return {
            viewModelScope.launch(dispatcherProvider.default) {
                navigationReducer.update(NavigationAction.GoTo(NavigationPage.SetupExerciseScreen))
                exerciseEventReducer.update(mapSetupExerciseAction(it))
            }
        }
    }

    private fun mapSetupExerciseAction(exercise: Exercise): ExerciseEventAction {
        return when (val endPage = eventState.endPage) {
            is Routines -> getRoutineSetupExerciseAction(exercise, endPage.routine)
            is Stats -> getStatsEventAction(exercise)
        }
    }

    private fun getRoutineSetupExerciseAction(
        exercise: Exercise,
        routine: Routine
    ) : ExerciseEventAction.SetupExercise {
        return ExerciseEventAction.SetupExercise(
            exercise = exercise,
            setupEndPage = ExerciseEventState.SetupEndPage.Routines(routine)
        )
    }

    private fun getStatsEventAction(exercise: Exercise): ExerciseEventAction {
        exercise.name // will use later when I set up stats
        return ExerciseEventAction.None
    }
}
