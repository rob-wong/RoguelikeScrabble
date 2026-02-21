package com.cypherose.features.dialogs.presentation.viewmodel

import com.cypherose.features.dialogs.presentation.state.DialogReducer
import com.cypherose.features.dialogs.presentation.state.DialogReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DialogModule {

	@Provides
	@Singleton
	fun provideDialogReducer(): DialogReducer {
		return DialogReducerImpl()
	}
}
