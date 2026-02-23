package com.cypherose.features.game.presentation.viewmodel

import com.cypherose.features.game.presentation.state.GameScreenReducer
import com.cypherose.features.game.presentation.state.GameScreenReducerImpl
import com.cypherose.features.game.presentation.state.MidshopOptionStateMapper
import com.cypherose.features.game.presentation.state.MidshopOptionStateMapperImpl
import com.cypherose.features.game.presentation.state.PreviouslyPlayedEffectsMapper
import com.cypherose.features.game.presentation.state.PreviouslyPlayedEffectsMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GameScreenModule {

	@Provides
	fun provideGameScreenReducer(
		midshopOptionStateMapper: MidshopOptionStateMapper,
		previouslyPlayedEffectsMapper: PreviouslyPlayedEffectsMapper
	): GameScreenReducer {
		return GameScreenReducerImpl(midshopOptionStateMapper, previouslyPlayedEffectsMapper)
	}

	@Provides
	fun provideMidshopOptionStateMapper(): MidshopOptionStateMapper {
		return MidshopOptionStateMapperImpl()
	}

	@Provides
	fun providePreviouslyPlayedEffectsMapper(): PreviouslyPlayedEffectsMapper {
		return PreviouslyPlayedEffectsMapperImpl()
	}
}
