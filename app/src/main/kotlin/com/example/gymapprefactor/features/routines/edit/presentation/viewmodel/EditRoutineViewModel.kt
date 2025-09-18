package com.example.gymapprefactor.features.routines.edit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineState
import kotlinx.coroutines.flow.Flow

abstract class EditRoutineViewModel : ViewModel() {

    abstract val state: Flow<EditRoutineState>
}
