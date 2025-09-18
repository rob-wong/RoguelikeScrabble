package com.example.gymapprefactor.features.routines.base.presentation.viewmodel.di

import com.example.gymapprefactor.features.routines.base.presentation.mappers.RoutineToButtonStateMapper
import com.example.gymapprefactor.features.routines.base.presentation.mappers.RoutineToButtonStateMapperImpl
import com.example.gymapprefactor.features.routines.base.presentation.state.BaseRoutineScreenReducer
import com.example.gymapprefactor.features.routines.base.presentation.state.BaseRoutineScreenReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BaseRoutineScreenModule {
    @Provides
    fun provideBaseRoutineScreenReducer(): BaseRoutineScreenReducer {
        return BaseRoutineScreenReducerImpl()
    }

    @Provides
    fun provideRoutineToButtonStateMapper(): RoutineToButtonStateMapper {
        return RoutineToButtonStateMapperImpl()
    }
}
