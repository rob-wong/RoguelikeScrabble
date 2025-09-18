package com.example.gymapprefactor.features.exercises.pick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseState
import kotlinx.coroutines.flow.Flow

abstract class PickExerciseViewModel : ViewModel() {

    abstract val state: Flow<PickExerciseState>
}
