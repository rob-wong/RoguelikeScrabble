package com.example.gymapprefactor.features.routines.base.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState
import kotlinx.coroutines.flow.Flow

abstract class BaseRoutineScreenViewModel : ViewModel() {

    abstract val state: Flow<BaseRoutineScreenState>
}
