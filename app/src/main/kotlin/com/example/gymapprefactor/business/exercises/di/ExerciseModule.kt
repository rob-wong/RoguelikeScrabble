package com.example.gymapprefactor.business.exercises.di

import com.example.gymapprefactor.business.exercises.data.ExerciseDataSource
import com.example.gymapprefactor.business.exercises.data.ExerciseRepositoryImpl
import com.example.gymapprefactor.business.exercises.domain.ExerciseRepository
import com.example.gymapprefactor.business.exercises.domain.GetExercisesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {

    @Provides
    fun provideExerciseDataSource(): ExerciseDataSource {
        return ExerciseDataSource()
    }

    @Provides
    fun provideExerciseRepository(
        dataSource: ExerciseDataSource
    ): ExerciseRepository {
        return ExerciseRepositoryImpl(dataSource)
    }

    @Provides
    fun provideGetExercisesUseCase(
        repository: ExerciseRepository
    ): GetExercisesUseCase {
        return GetExercisesUseCase(repository)
    }
}
