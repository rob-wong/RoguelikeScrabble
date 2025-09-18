package com.example.gymapprefactor.features.exercises.pick.presentation.viewmodel.di

import com.example.gymapprefactor.features.exercises.pick.presentation.mappers.PickTemplateToButtonListMapper
import com.example.gymapprefactor.features.exercises.pick.presentation.mappers.PickTemplateToButtonListMapperImpl
import com.example.gymapprefactor.features.exercises.pick.presentation.state.PickExerciseReducer
import com.example.gymapprefactor.features.exercises.pick.presentation.state.PickExerciseReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PickExerciseModule {

    @Provides
    fun providePickExerciseReducer() : PickExerciseReducer {
        return PickExerciseReducerImpl()
    }

    @Provides
    fun providePickExerciseStateToButtonListMapper(): PickTemplateToButtonListMapper {
        return PickTemplateToButtonListMapperImpl()
    }
}
