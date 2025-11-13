package com.example.gymapprefactor.common.components.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.common.components.presentation.ScreenBackgroundState
import com.example.gymapprefactor.common.components.presentation.models.BackgroundAction
import com.example.gymapprefactor.common.components.presentation.state.BackgroundReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BackgroundViewModel : ViewModel() {
    abstract val state: Flow<ScreenBackgroundState>
    abstract fun setBackground(background: ScreenBackgroundState)
}

@HiltViewModel
class BackgroundViewModelImpl @Inject constructor(
    private val backgroundReducer: BackgroundReducer,
    private val dispatcherProvider: DispatcherProvider,
) : BackgroundViewModel() {

    override val state = backgroundReducer.state

    override fun setBackground(background: ScreenBackgroundState) {
        viewModelScope.launch(dispatcherProvider.main) {
            backgroundReducer.update(BackgroundAction.SetBackground(background))
        }
    }
}
