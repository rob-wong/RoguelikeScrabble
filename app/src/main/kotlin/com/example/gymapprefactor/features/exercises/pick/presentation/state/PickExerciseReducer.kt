package com.example.gymapprefactor.features.exercises.pick.presentation.state

import com.example.gymapprefactor.business.interfaces.Reducer
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseAction
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseState

interface PickExerciseReducer : Reducer<PickExerciseState, PickExerciseAction>
