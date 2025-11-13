package com.example.gymapprefactor.common.components.presentation

import com.example.gymapprefactor.common.components.buttons.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.common.components.buttons.presentation.adapters.RectangleButtonMapperImpl
import com.example.gymapprefactor.common.components.presentation.state.BackgroundReducer
import com.example.gymapprefactor.common.components.presentation.state.BackgroundReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ComponentModule {

	@Provides
	fun provideRectangleButtonMapper(): RectangleButtonMapper {
		return RectangleButtonMapperImpl()
	}

	@Provides
	@Singleton
	fun provideBackgroundReducer(): BackgroundReducer {
		return BackgroundReducerImpl()
	}
}
