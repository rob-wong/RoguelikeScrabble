package com.example.gymapprefactor.features.navigation.presentation.viewmodel.di

import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @Singleton
    fun provideNavigationReducer(): NavigationReducer {
        return NavigationReducerImpl()
    }
}
