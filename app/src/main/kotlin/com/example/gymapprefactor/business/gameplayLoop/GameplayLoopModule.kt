package com.example.gymapprefactor.business.gameplayLoop

import android.content.Context
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
