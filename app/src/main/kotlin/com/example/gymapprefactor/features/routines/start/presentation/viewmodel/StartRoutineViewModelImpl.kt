package com.example.gymapprefactor.features.routines.start.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineAction
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState
import com.example.gymapprefactor.features.routines.start.presentation.state.StartRoutineReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartRoutineViewModelImpl @Inject constructor(
    private val startRoutineReducer: StartRoutineReducer,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
) : StartRoutineViewModel() {
    override val state: Flow<StartRoutineState> = startRoutineReducer.state

    init {
         setContent()
    }

    private fun setContent() {
        viewModelScope.launch(dispatcherProvider.default) {
            startRoutineReducer.update(StartRoutineAction.SetContent(
                topBarState = getTopBar()
            ))
        }
    }

    private fun getTopBar(): TopBarState {
        return TopBarState.Content(
            title = "Start Routine",
            onBack = ::onBack
        )
    }

    private fun onBack() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoBack)
        }
    }
}
