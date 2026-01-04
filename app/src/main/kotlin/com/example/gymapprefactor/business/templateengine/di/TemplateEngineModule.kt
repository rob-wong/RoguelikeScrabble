package com.example.gymapprefactor.business.templateengine.di

import com.example.gymapprefactor.business.templateengine.data.TemplateDataSource
import com.example.gymapprefactor.business.templateengine.data.TemplateRepositoryImpl
import com.example.gymapprefactor.business.templateengine.domain.TemplateEngine
import com.example.gymapprefactor.business.templateengine.domain.TemplateRepository
import com.example.gymapprefactor.business.templateengine.domain.usecases.LoadShopScreenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TemplateEngineModule {
	@Provides
	fun provideTemplateRepository(
		dataSource: TemplateDataSource
	): TemplateRepository {
		return TemplateRepositoryImpl(dataSource)
	}

	@Provides
	fun provideLoadShopScreenUseCase(
		repository: TemplateRepository,
		templateEngine: TemplateEngine
	): LoadShopScreenUseCase {
		return LoadShopScreenUseCase(repository, templateEngine)
	}
}
