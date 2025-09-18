package com.example.gymapprefactor.features.routines.base.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.routines.domain.GetRoutinesUseCase
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.routines.base.presentation.mappers.RoutineToButtonStateMapper
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenAction
import com.example.gymapprefactor.features.routines.base.presentation.state.BaseRoutineScreenReducer
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseRoutineScreenViewModelImpl @Inject constructor(
    private val baseRoutineScreenReducer: BaseRoutineScreenReducer,
    private val routineEventReducer: RoutineEventReducer,
    private val getRoutinesUseCase: GetRoutinesUseCase,
    private val routineToButtonStateMapper: RoutineToButtonStateMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val navigationReducer: NavigationReducer,
) : BaseRoutineScreenViewModel() {
    override val state = baseRoutineScreenReducer.state

    init {
        setContent()
        initCollection()
    }

    private fun initCollection() {
        viewModelScope.launch(dispatcherProvider.default) {
            navigationReducer.state.collect {
                when (it) {
                    is NavigationState.CurrentPage ->
                        if(it.page == NavigationPage.BaseRoutineScreen) { setContent() }
                    else -> Unit
                }
            }
        }
    }

    private fun setContent() {
        viewModelScope.launch(dispatcherProvider.default) {
            baseRoutineScreenReducer.update(
                BaseRoutineScreenAction.SetContent(
                    routineButtons = getRoutineButtons(),
                    createRoutineButton = getCreateRoutineButton(),
                    topBarState = getTopBar()
                )
            )
        }
    }

    private suspend fun getRoutineButtons(): List<RectangleButtonState> {
        return getRoutinesUseCase.invoke().map {
            routineToButtonStateMapper.map(
                RoutineToButtonStateMapper.Param(
                    routine = it,
                    navigationCallback = ::goToEditRoutine,
                    editRoutineEventCallback = ::sendRoutineToEditPage,
                )
            )
        }
    }

    private fun getCreateRoutineButton(): RectangleButtonState {
        return RectangleButtonState.Content(
            onClick = {
                goToEditRoutine()
                sendCreateRoutineToEditPage()
            },
            text = "Create Routine"
        )
    }

    private fun goToEditRoutine() {
        viewModelScope.launch(dispatcherProvider.default) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
        }
    }

    private fun sendRoutineToEditPage(routine: Routine) {
        viewModelScope.launch(dispatcherProvider.default) {
            routineEventReducer.update(EditRoutineEventAction.SelectRoutine(routine))
        }
    }

    private fun sendCreateRoutineToEditPage() {
        viewModelScope.launch(dispatcherProvider.default) {
            routineEventReducer.update(EditRoutineEventAction.CreateRoutine)
        }
    }

    private fun getTopBar(): TopBarState {
        return TopBarState.Content(
            title = "Routines",
            onBack = ::onBack
        )
    }

    private fun onBack() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoBack)
        }
    }
}
