package com.example.gymapprefactor.features.routines.start.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState
import kotlinx.coroutines.flow.Flow

abstract class StartRoutineViewModel : ViewModel() {

    abstract val state: Flow<StartRoutineState>
}
