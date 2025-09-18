package com.example.gymapprefactor.features.exercises.setup.presentation.viewmodel.di

import com.example.gymapprefactor.features.exercises.setup.presentation.state.SetupExerciseReducer
import com.example.gymapprefactor.features.exercises.setup.presentation.state.SetupExerciseReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SetupExerciseModule {

    @Provides
    fun setupExerciseReducer() : SetupExerciseReducer {
        return SetupExerciseReducerImpl()
    }
}
