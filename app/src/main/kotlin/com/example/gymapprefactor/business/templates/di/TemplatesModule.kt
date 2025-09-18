package com.example.gymapprefactor.business.templates.di

import com.example.gymapprefactor.business.templates.data.ExerciseSetupsDataSource
import com.example.gymapprefactor.business.templates.data.TemplatesRepositoryImpl
import com.example.gymapprefactor.business.templates.domain.CreateTemplateUseCase
import com.example.gymapprefactor.business.templates.domain.TemplatesRepository
import com.example.gymapprefactor.business.templates.domain.GetTemplatesUseCase
import com.example.gymapprefactor.business.templates.domain.SaveTemplateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TemplatesModule {
    @Provides
    fun provideExerciseSetupDataSource(): ExerciseSetupsDataSource {
        return ExerciseSetupsDataSource()
    }

    @Provides
    fun provideExerciseSetupsRepository(
        dataSource: ExerciseSetupsDataSource
    ): TemplatesRepository {
        return TemplatesRepositoryImpl(dataSource)
    }

    @Provides
    fun provideGetExerciseSetupsUseCase(
        repository: TemplatesRepository,
    ): GetTemplatesUseCase {
        return GetTemplatesUseCase(repository)
    }

    @Provides
    fun provideSaveExerciseSetupUseCase(
        repository: TemplatesRepository,
    ): SaveTemplateUseCase {
        return SaveTemplateUseCase(repository)
    }

    @Provides
    fun provideCreateExerciseSetupUseCase(
        saveTemplateUseCase: SaveTemplateUseCase
    ): CreateTemplateUseCase {
        return CreateTemplateUseCase(saveTemplateUseCase)
    }
}
