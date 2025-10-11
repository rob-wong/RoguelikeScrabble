package com.example.gymapprefactor.business.gameplayLoop.di

import android.content.Context
import com.example.gymapprefactor.business.gameplayLoop.data.GameplayDataSource
import com.example.gymapprefactor.business.gameplayLoop.data.GameplayRepositoryImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.EndGameUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayBusinessMediator
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.gameplayLoop.domain.GetGameStateUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.SaveGameStateUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.WordValidityMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.WordValidityMapperImpl
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
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
	fun provideGameplayDataSource(
		appDataModel : AppDataModel
	): GameplayDataSource {
		return GameplayDataSource(appDataModel)
	}

	@Provides
	fun provideGetGameStateUseCase(
		repository: GameplayRepository
	): GetGameStateUseCase {
		return GetGameStateUseCase(repository)
	}

	@Provides
	fun provideSaveGameStateUseCase(
		repository: GameplayRepository
	): SaveGameStateUseCase {
		return SaveGameStateUseCase(repository)
	}

	@Provides
	fun provideEndGameUseCase(
		repository: GameplayRepository
	): EndGameUseCase {
		return EndGameUseCase(repository)
	}

	@Provides
	fun provideGameplayRepository(
		dataSource: GameplayDataSource,
	): GameplayRepository {
		return GameplayRepositoryImpl(dataSource)
	}

	@Provides
	fun provideGameplayBusinessMediator(
		getGameStateUseCase: GetGameStateUseCase,
		saveGameStateUseCase: SaveGameStateUseCase,
		userBusinessMediator: UserBusinessMediator,
		endGameUseCase: EndGameUseCase,
		drawHandMapper: DrawHandMapper,
	): GameplayBusinessMediator {
		return GameplayBusinessMediator(
			getGameStateUseCase = getGameStateUseCase,
			saveGameStateUseCase = saveGameStateUseCase,
			endGameUseCase = endGameUseCase,
			userBusinessMediator = userBusinessMediator,
			drawHandMapper = drawHandMapper,
		)
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
	fun provideWordValidityMapper(validWords: Set<String>): WordValidityMapper {
		return WordValidityMapperImpl(validWords)
	}

	@Provides
	fun provideDrawHandMapper(): DrawHandMapper {
		return DrawHandMapperImpl()
	}
}
