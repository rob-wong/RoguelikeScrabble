package com.cypherose.features.homeScreen.presentation.viewmodel.di

import com.cypherose.features.homeScreen.presentation.state.HomeScreenReducer
import com.cypherose.features.homeScreen.presentation.state.HomeScreenReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HomeScreenModule {
    @Provides
    fun provideHomeScreenReducer(): HomeScreenReducer {
        return HomeScreenReducerImpl()
    }
}
