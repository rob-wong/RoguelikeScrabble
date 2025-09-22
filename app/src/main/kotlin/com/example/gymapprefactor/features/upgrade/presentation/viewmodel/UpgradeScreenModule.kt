package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducer
import com.example.gymapprefactor.features.upgrade.presentation.state.UpgradeScreenReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UpgradeScreenModule {
	@Provides
	fun provideUpgradeScreenReducer(): UpgradeScreenReducer {
		return UpgradeScreenReducerImpl()
	}
}
