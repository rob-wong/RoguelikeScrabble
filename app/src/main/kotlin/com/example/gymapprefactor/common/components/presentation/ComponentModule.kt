package com.example.gymapprefactor.common.components.presentation

import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ComponentModule {

	@Provides
	fun provideRectangleButtonMapper(): RectangleButtonMapper {
		return RectangleButtonMapperImpl()
	}
}
