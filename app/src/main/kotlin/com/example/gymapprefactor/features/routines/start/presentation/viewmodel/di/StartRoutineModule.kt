package com.example.gymapprefactor.features.routines.start.presentation.viewmodel.di

import com.example.gymapprefactor.features.routines.start.presentation.state.StartRoutineReducer
import com.example.gymapprefactor.features.routines.start.presentation.state.StartRoutineReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object StartRoutineModule {
    @Provides
    fun provideStartRoutineReducer() : StartRoutineReducer {
        return StartRoutineReducerImpl()
    }
}
