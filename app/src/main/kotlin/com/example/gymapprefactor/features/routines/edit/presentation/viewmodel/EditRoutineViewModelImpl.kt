package com.example.gymapprefactor.features.routines.edit.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.copy
import com.example.gymapprefactor.business.models.removeTemplate
import com.example.gymapprefactor.business.routines.domain.CreateRoutineUseCase
import com.example.gymapprefactor.business.routines.domain.SaveRoutineUseCase
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventAction
import com.example.gymapprefactor.features.exercises.events.ExerciseEventReducer
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.routines.base.presentation.mappers.TemplateToButtonStateMapper
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventState
import com.example.gymapprefactor.features.routines.edit.presentation.state.EditRoutineReducer
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("LongParameterList")
class EditRoutineViewModelImpl @Inject constructor(
    private val editRoutineReducer: EditRoutineReducer,
    private val routineEventReducer: RoutineEventReducer,
    private val createRoutineUseCase: CreateRoutineUseCase,
    private val saveRoutineUseCase: SaveRoutineUseCase,
    private val templateToButtonStateMapper: TemplateToButtonStateMapper,
    private val exerciseEventReducer: ExerciseEventReducer,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
) : EditRoutineViewModel() {
    override val state = editRoutineReducer.state
    private lateinit var currentRoutine: Routine
    private var editMode: Boolean = false

    init {
        initCollection()
    }

    private fun initCollection() {
        viewModelScope.launch(dispatcherProvider.default) {
            routineEventReducer.state.collect {
                when (it) {
                    is EditRoutineEventState.SelectRoutine -> setContent(
                        routine = it.routine,
                        isEditMode = true,
                    )
                    is EditRoutineEventState.RoutineUpdated -> routineUpdated(it.routine)
                    is EditRoutineEventState.CreateRoutine -> createRoutine()
                    is EditRoutineEventState.None -> Unit
                }
            }
        }
    }

    private suspend fun setContent(
        routine: Routine,
        isEditMode: Boolean,
    ) {
        currentRoutine = routine
        editMode = isEditMode
        editRoutineReducer.update(
            EditRoutineAction.SetContent(
                exerciseSetupList = getExistingTemplateButtons(routine),
                onClickAddExercise = ::onClickAddExercise,
                nameInputField = getNameInputField(routine),
                onSave = ::onSave,
                topBarState = getTopBar(isEditMode)
            )
        )
    }

    private suspend fun routineUpdated(
        routine: Routine,
    ) {
        if (contentIsUpdatedRoutine(routine)) {
            setContent(routine, editMode)
        }
    }

    private fun contentIsUpdatedRoutine(updatedRoutine: Routine): Boolean {
        return (updatedRoutine.id == currentRoutine.id)
    }

    private suspend fun createRoutine() {
        setContent(routine = createRoutineUseCase(), isEditMode = false)
    }

    private fun getTopBar(isEditMode: Boolean): TopBarState {
        return TopBarState.Content(
            title = getTitleState(isEditMode),
            onBack = ::onBack
        )
    }

    private fun onBack() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoBack)
        }
    }

    private fun getTitleState(isEditMode: Boolean): String {
        return if (isEditMode) {
            "Edit Routine"
        } else {
            "Create Routine"
        }
    }

    private fun getExistingTemplateButtons(routine: Routine): List<ButtonState> {
        return routine.templates.map {
            templateToButtonStateMapper.map(
                TemplateToButtonStateMapper.Param(
                    template = it,
                    removeExerciseCallback = ::onClickRemoveExercise,
                    editExerciseCallback = { },
                )
            )
        }
    }

    private fun onClickAddExercise() {
        viewModelScope.launch(dispatcherProvider.default) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.PickExerciseScreen))
            exerciseEventReducer.update(
                ExerciseEventAction.PickExercise(
                    ExerciseEventState.PickEndPage.Routines(currentRoutine)
                )
            )
        }
    }

    private fun onClickRemoveExercise(id: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            currentRoutine = (currentRoutine as DefaultRoutine).removeTemplate(id)
            saveRoutineUseCase(currentRoutine)
            setContent(currentRoutine, editMode)
        }
    }

    private fun getNameInputField(routine: Routine): TextInputFieldState {
        return TextInputFieldState.Content(
            text = routine.name,
            onValueChanged = ::onNameValueChanged,
            placeholder = "Enter Name Here",
        )
    }

    private fun onNameValueChanged(newValue: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            saveRoutineUseCase((currentRoutine as DefaultRoutine).copy(name = newValue))
        }
    }

    // editing fields automatically saves, Button is for decoration
    private fun onSave() {
        onBack()
    }
}
