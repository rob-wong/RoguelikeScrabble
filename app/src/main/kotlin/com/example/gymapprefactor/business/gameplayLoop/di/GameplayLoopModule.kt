package com.example.gymapprefactor.business.gameplayLoop.di

import android.content.Context
import com.example.gymapprefactor.business.gameplayLoop.data.GameplayDataSource
import com.example.gymapprefactor.business.gameplayLoop.data.GameplayRepositoryImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.gameplayLoop.domain.WordValidityMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.WordValidityMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameplayLoopModule {

	@Provides
	fun provideGameplayDataSource(): GameplayDataSource {
		return GameplayDataSource()
	}

	@Provides
	fun provideGameplayRepository(
		dataSource: GameplayDataSource
	): GameplayRepository {
		return GameplayRepositoryImpl(dataSource)
	}

	@Provides
	@Singleton
	fun provideValidWords(@ApplicationContext context: Context): Set<String> {
		val input = context.assets.open("words.txt")
		return input.bufferedReader()
			.readLines()
			.map { it.lowercase() }
			.toSet()
	}

	@Provides
	@Singleton
	fun provideWordValidityMapper(validWords: Set<String>): WordValidityMapper {
		return WordValidityMapperImpl(validWords)
	}
}
