package com.example.gymapprefactor.features.game.presentation.viewmodel

import com.example.gymapprefactor.features.game.presentation.state.GameScreenReducer
import com.example.gymapprefactor.features.game.presentation.state.GameScreenReducerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GameScreenModule {

	@Provides
	fun provideGameScreenReducer(): GameScreenReducer {
		return GameScreenReducerImpl()
	}
}
