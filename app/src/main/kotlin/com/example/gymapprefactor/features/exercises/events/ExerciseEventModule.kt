package com.example.gymapprefactor.features.exercises.events

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExerciseEventModule {

    @Provides
    @Singleton
    fun provideExerciseEventReducer() : ExerciseEventReducer {
        return ExerciseEventReducerImpl()
    }
}
