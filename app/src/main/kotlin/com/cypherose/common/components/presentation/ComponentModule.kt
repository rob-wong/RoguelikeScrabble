package com.cypherose.common.components.presentation

import com.cypherose.common.components.buttons.presentation.adapters.RectangleButtonMapper
import com.cypherose.common.components.buttons.presentation.adapters.RectangleButtonMapperImpl
import com.cypherose.common.components.presentation.state.BackgroundReducer
import com.cypherose.common.components.presentation.state.BackgroundReducerImpl
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
