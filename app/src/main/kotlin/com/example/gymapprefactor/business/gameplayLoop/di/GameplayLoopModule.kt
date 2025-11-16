package com.example.gymapprefactor.business.gameplayLoop.di

import android.content.Context
import com.example.gymapprefactor.business.gameplayLoop.data.GameplayDataSource
import com.example.gymapprefactor.business.gameplayLoop.data.GameplayRepositoryImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.AdvanceToNextEnemyUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.ApplyScoreToEnemyUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.CheckGameConditionsUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.CreateGameUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.EndGameUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.GameRules
import com.example.gymapprefactor.business.gameplayLoop.domain.GameRulesImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayBusinessMediator
import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.gameplayLoop.domain.GetGameStateUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.PlayWordUseCase
import com.example.gymapprefactor.business.gameplayLoop.domain.SaveGameStateUseCase
import com.example.gymapprefactor.business.effects.data.EffectsDataSource
import com.example.gymapprefactor.business.effects.data.EffectsRepositoryImpl
import com.example.gymapprefactor.business.effects.domain.EffectsRepository
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessorFactory
import com.example.gymapprefactor.business.effects.templating.domain.processors.ComboProcessor
import com.example.gymapprefactor.business.effects.templating.domain.processors.FixedAdditionProcessor
import com.example.gymapprefactor.business.effects.templating.domain.processors.MultiplicationProcessor
import com.example.gymapprefactor.business.gameplayLoop.domain.EffectScoreMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.EffectScoreMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.ScoreWordMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.ScoreWordMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.WordValidityMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.WordValidityMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyCreationMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyCreationMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyLabelMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EnemyLabelMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.DiscardsRemainingMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.DiscardsRemainingMapperImpl
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EffectAnimationPayloadMapper
import com.example.gymapprefactor.business.gameplayLoop.domain.mappers.EffectAnimationPayloadMapperImpl
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
@Suppress("TooManyFunctions")
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

	@Provides
	fun provideScoreWordMapper(): ScoreWordMapper {
		return ScoreWordMapperImpl()
	}

	@Provides
	fun provideEffectsDataSource(
		@ApplicationContext context: Context,
		dispatcherProvider: com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
	): EffectsDataSource {
		return EffectsDataSource(context, dispatcherProvider)
	}

	@Provides
	fun provideEffectsRepository(
		dataSource: EffectsDataSource
	): EffectsRepository {
		return EffectsRepositoryImpl(dataSource)
	}

	@Provides
	@Singleton
	fun provideJson(): kotlinx.serialization.json.Json {
		return kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
	}

	@Provides
	fun provideFixedAdditionProcessor(
		json: kotlinx.serialization.json.Json
	): FixedAdditionProcessor {
		return FixedAdditionProcessor(json)
	}

	@Provides
	fun provideMultiplicationProcessor(
		json: kotlinx.serialization.json.Json
	): MultiplicationProcessor {
		return MultiplicationProcessor(json)
	}

	@Provides
	fun provideComboProcessor(
		json: kotlinx.serialization.json.Json,
		processorFactoryProvider: javax.inject.Provider<EffectProcessorFactory>
	): ComboProcessor {
		return ComboProcessor(json, processorFactoryProvider)
	}

	@Provides
	@Singleton
	fun provideEffectProcessorFactory(
		fixedAdditionProcessor: FixedAdditionProcessor,
		multiplicationProcessor: MultiplicationProcessor,
		comboProcessor: ComboProcessor
	): EffectProcessorFactory {
		return EffectProcessorFactory(
			fixedAdditionProcessor = fixedAdditionProcessor,
			multiplicationProcessor = multiplicationProcessor,
			comboProcessor = comboProcessor
		)
	}

	@Provides
	fun provideEffectScoreMapper(
		effectsRepository: EffectsRepository,
		processorFactory: EffectProcessorFactory,
		json: kotlinx.serialization.json.Json
	): EffectScoreMapper {
		return EffectScoreMapperImpl(effectsRepository, processorFactory, json)
	}

	@Provides
	fun provideEnemyCreationMapper(): EnemyCreationMapper {
		return EnemyCreationMapperImpl()
	}

	@Provides
	fun provideEnemyLabelMapper(): EnemyLabelMapper {
		return EnemyLabelMapperImpl()
	}

	@Provides
	fun provideDiscardsRemainingMapper(): DiscardsRemainingMapper {
		return DiscardsRemainingMapperImpl()
	}

	@Provides
	fun provideEffectAnimationPayloadMapper(): EffectAnimationPayloadMapper {
		return EffectAnimationPayloadMapperImpl()
	}

	@Provides
	fun provideGameRules(): GameRules {
		return GameRulesImpl()
	}

	@Provides
	fun provideDrawHandUseCase(
		drawHandMapper: DrawHandMapper
	): DrawHandUseCase {
		return DrawHandUseCase(drawHandMapper)
	}

	@Provides
	fun providePlayWordUseCase(
		wordValidityMapper: WordValidityMapper,
		drawHandUseCase: DrawHandUseCase,
		scoreWordMapper: ScoreWordMapper,
		saveGameStateUseCase: SaveGameStateUseCase
	): PlayWordUseCase {
		return PlayWordUseCase(
			wordValidityMapper = wordValidityMapper,
			drawHandUseCase = drawHandUseCase,
			scoreWordMapper = scoreWordMapper,
			saveGameStateUseCase = saveGameStateUseCase
		)
	}

	@Provides
	fun provideCreateGameUseCase(
		userBusinessMediator: UserBusinessMediator,
		drawHandUseCase: DrawHandUseCase,
		saveGameStateUseCase: SaveGameStateUseCase,
		enemyCreationMapper: EnemyCreationMapper
	): CreateGameUseCase {
		return CreateGameUseCase(
			userBusinessMediator = userBusinessMediator,
			drawHandUseCase = drawHandUseCase,
			saveGameStateUseCase = saveGameStateUseCase,
			enemyCreationMapper = enemyCreationMapper
		)
	}

	@Provides
	fun provideApplyScoreToEnemyUseCase(
		gameRules: GameRules
	): ApplyScoreToEnemyUseCase {
		return ApplyScoreToEnemyUseCase(gameRules)
	}

	@Provides
	fun provideAdvanceToNextEnemyUseCase(
		enemyCreationMapper: EnemyCreationMapper,
		drawHandUseCase: DrawHandUseCase
	): AdvanceToNextEnemyUseCase {
		return AdvanceToNextEnemyUseCase(
			enemyCreationMapper = enemyCreationMapper,
			drawHandUseCase = drawHandUseCase
		)
	}

	@Provides
	fun provideCheckGameConditionsUseCase(
		gameRules: GameRules,
		advanceToNextEnemyUseCase: AdvanceToNextEnemyUseCase
	): CheckGameConditionsUseCase {
		return CheckGameConditionsUseCase(gameRules, advanceToNextEnemyUseCase)
	}

	@Provides
	fun provideGameplayBusinessMediator(
		getGameStateUseCase: GetGameStateUseCase,
		saveGameStateUseCase: SaveGameStateUseCase,
		endGameUseCase: EndGameUseCase,
		playWordUseCase: PlayWordUseCase,
		drawHandUseCase: DrawHandUseCase,
		createGameUseCase: CreateGameUseCase,
		applyScoreToEnemyUseCase: ApplyScoreToEnemyUseCase,
		effectScoreMapper: EffectScoreMapper,
		gameRules: GameRules,
		advanceToNextEnemyUseCase: AdvanceToNextEnemyUseCase
	): GameplayBusinessMediator {
		return GameplayBusinessMediator(
			getGameStateUseCase = getGameStateUseCase,
			saveGameStateUseCase = saveGameStateUseCase,
			endGameUseCase = endGameUseCase,
			playWordUseCase = playWordUseCase,
			drawHandUseCase = drawHandUseCase,
			createGameUseCase = createGameUseCase,
			applyScoreToEnemyUseCase = applyScoreToEnemyUseCase,
			effectScoreMapper = effectScoreMapper,
			gameRules = gameRules,
			advanceToNextEnemyUseCase = advanceToNextEnemyUseCase
		)
	}
}
