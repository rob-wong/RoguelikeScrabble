package com.example.gymapprefactor.business.routines.di

import com.example.gymapprefactor.business.routines.data.RoutineDataSource
import com.example.gymapprefactor.business.routines.data.RoutinesRepositoryImpl
import com.example.gymapprefactor.business.routines.domain.CreateRoutineUseCase
import com.example.gymapprefactor.business.routines.domain.GetRoutinesUseCase
import com.example.gymapprefactor.business.routines.domain.RoutinesRepository
import com.example.gymapprefactor.business.routines.domain.SaveRoutineUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoutinesModule {
    @Provides
    fun provideRoutineDataSource(): RoutineDataSource {
        return RoutineDataSource()
    }

    @Provides
    fun provideRoutineRepository(
        dataSource: RoutineDataSource
    ): RoutinesRepository {
        return RoutinesRepositoryImpl(dataSource)
    }

    @Provides
    fun provideGetRoutinesUseCase(
        repository: RoutinesRepository
    ): GetRoutinesUseCase {
        return GetRoutinesUseCase(repository)
    }

    @Provides
    fun provideSaveRoutineUseCase(
        repository: RoutinesRepository
    ): SaveRoutineUseCase {
        return SaveRoutineUseCase(repository)
    }

    @Provides
    fun provideCreateRoutineUseCase(
        saveRoutineUseCase: SaveRoutineUseCase,
    ): CreateRoutineUseCase {
        return CreateRoutineUseCase(saveRoutineUseCase)
    }
}
