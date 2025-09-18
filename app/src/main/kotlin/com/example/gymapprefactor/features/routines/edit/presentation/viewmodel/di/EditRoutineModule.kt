package com.example.gymapprefactor.features.routines.edit.presentation.viewmodel.di

import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.features.routines.base.presentation.mappers.TemplateToButtonStateMapper
import com.example.gymapprefactor.features.routines.base.presentation.mappers.TemplateToButtonStateMapperImpl
import com.example.gymapprefactor.features.routines.edit.presentation.state.EditRoutineReducer
import com.example.gymapprefactor.features.routines.edit.presentation.state.EditRoutineReducerImpl
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducer
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EditRoutineModule {
    @Provides
    fun provideEditRoutineReducer(
        rectangleButtonMapper: RectangleButtonMapper
    ): EditRoutineReducer {
        return EditRoutineReducerImpl(rectangleButtonMapper)
    }

    @Provides
    @Singleton
    fun provideEditRoutineEventReducer(): RoutineEventReducer {
        return RoutineEventReducerImpl()
    }

    @Provides
    fun provideExerciseSetupToButtonStateMapper(): TemplateToButtonStateMapper {
        return TemplateToButtonStateMapperImpl()
    }
}
