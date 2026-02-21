package com.cypherose.features.game.presentation.viewmodel

import com.cypherose.features.game.presentation.state.GameScreenReducer
import com.cypherose.features.game.presentation.state.GameScreenReducerImpl
import com.cypherose.features.game.presentation.state.MidshopOptionStateMapper
import com.cypherose.features.game.presentation.state.MidshopOptionStateMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GameScreenModule {

	@Provides
	fun provideGameScreenReducer(
		midshopOptionStateMapper: MidshopOptionStateMapper
	): GameScreenReducer {
		return GameScreenReducerImpl(midshopOptionStateMapper)
	}

	@Provides
	fun provideMidshopOptionStateMapper(): MidshopOptionStateMapper {
		return MidshopOptionStateMapperImpl()
	}
}
