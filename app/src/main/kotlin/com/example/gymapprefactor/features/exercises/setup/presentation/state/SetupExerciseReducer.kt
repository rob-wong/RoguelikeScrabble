package com.example.gymapprefactor.features.exercises.setup.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseAction
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseState

interface SetupExerciseReducer : Reducer<SetupExerciseState, SetupExerciseAction>
