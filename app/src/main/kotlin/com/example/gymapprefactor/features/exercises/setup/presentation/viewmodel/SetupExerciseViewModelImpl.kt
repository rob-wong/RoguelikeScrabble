package com.example.gymapprefactor.features.exercises.setup.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.templates.domain.CreateTemplateUseCase
import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.addSetupExercise
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import com.example.gymapprefactor.business.routines.domain.SaveRoutineUseCase
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventReducer
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState.*
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseAction
import com.example.gymapprefactor.features.exercises.setup.presentation.state.SetupExerciseReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("LongParameterList")
class SetupExerciseViewModelImpl @Inject constructor(
    private val setupExerciseReducer: SetupExerciseReducer,
    private val exerciseEventReducer: ExerciseEventReducer,
    private val routineEventReducer: RoutineEventReducer,
    private val createTemplateUseCase: CreateTemplateUseCase,
    private val saveRoutineUseCase: SaveRoutineUseCase,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
) : SetupExerciseViewModel() {
    override val state = setupExerciseReducer.state

    private lateinit var currentTemplate: Template

    init {
        initCollection()
    }

    private fun initCollection() {
        viewModelScope.launch(dispatcherProvider.default) {
            exerciseEventReducer.state.collect {
                when (it) {
                    is SetupExercise -> setContent(it)
                    else -> Unit
                }
            }
        }
    }

    private fun setContent(eventState: SetupExercise) {
        viewModelScope.launch(dispatcherProvider.default) {
            currentTemplate = createExerciseSetup(eventState.exercise)
            setupExerciseReducer.update(SetupExerciseAction.SetContent(
                exercise = eventState.exercise,
                onSave = mapOnSave(eventState),
                topBarState = getTopBar()
            ))
        }
    }

    private suspend fun createExerciseSetup(exercise: Exercise): Template {
        return createTemplateUseCase(exercise = exercise, sets = DEFAULT_SETS)
    }

    private fun mapOnSave(eventState: SetupExercise): RectangleButtonState {
        return when (eventState.setupEndPage) {
            is SetupEndPage.Begin -> mapBeginOnSave(eventState)
            is SetupEndPage.Routines -> mapRoutinesOnSave(eventState.setupEndPage.routine)
        }
    }

    private fun mapBeginOnSave(eventState: SetupExercise): RectangleButtonState {
        // unused parameters
        eventState.exercise

        return RectangleButtonState.None
    }

    private fun mapRoutinesOnSave(
        routine: Routine,
    ): RectangleButtonState {

        return RectangleButtonState.Content(
            text = "Save",
            onClick = {
                viewModelScope.launch(dispatcherProvider.main) {
                    val updatedRoutine = saveRoutineUseCase(
                        (routine as DefaultRoutine).addSetupExercise(currentTemplate)
                    )
                    routineEventReducer.update(EditRoutineEventAction.RoutineUpdated(updatedRoutine))
                    navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
                }
            }
        )
    }

    private fun getTopBar(): TopBarState {
        return TopBarState.Content(
            title = "Setup Exercise",
            onBack = ::onBack
        )
    }

    private fun onBack() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoBack)
        }
    }

    companion object {
        const val DEFAULT_SETS = 4
    }
}
