package com.example.gymapprefactor.features.exercises.setup.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseState
import kotlinx.coroutines.flow.Flow

abstract class SetupExerciseViewModel : ViewModel() {
    abstract val state: Flow<SetupExerciseState>
}
