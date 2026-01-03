package com.example.gymapprefactor.business.templateengine.di

import com.example.gymapprefactor.business.templateengine.data.TemplateDataSource
import com.example.gymapprefactor.business.templateengine.data.TemplateRepositoryImpl
import com.example.gymapprefactor.business.templateengine.domain.TemplateEngine
import com.example.gymapprefactor.business.templateengine.domain.TemplateRepository
import com.example.gymapprefactor.business.templateengine.domain.registry.TemplateRegistry
import com.example.gymapprefactor.business.templateengine.domain.registry.TemplateRegistryImpl
import com.example.gymapprefactor.business.templateengine.domain.usecases.LoadShopScreenUseCase
import com.example.gymapprefactor.features.templateengine.presentation.factories.basic.BasicListTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.factories.shopcard.ShopCardItemTemplateFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemplateEngineModule {
	@Provides
	@Singleton
	fun provideTemplateRegistry(): TemplateRegistry {
		val registry = TemplateRegistryImpl()
		registerTemplateFactories(registry)
		return registry
	}

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

	private fun registerTemplateFactories(registry: TemplateRegistry) {
		registry.registerListFactory("basic", BasicListTemplateFactory())
		registry.registerItemFactory("shopcard", ShopCardItemTemplateFactory())
	}
}
