package com.example.gymapprefactor.features.templateengine.presentation.di

import com.example.gymapprefactor.features.templateengine.presentation.factories.basic.BasicListTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.factories.shopcard.ShopCardItemTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.registry.TemplateRegistry
import com.example.gymapprefactor.features.templateengine.presentation.registry.TemplateRegistryImpl
import com.example.gymapprefactor.features.templateengine.presentation.services.TemplateContentService
import com.example.gymapprefactor.features.templateengine.presentation.services.TemplateContentServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TemplatePresentationModule {
	@Provides
	@Singleton
	fun provideTemplateRegistry(): TemplateRegistry {
		val registry = TemplateRegistryImpl()
		registerTemplateFactories(registry)
		return registry
	}

	@Provides
	@Singleton
	fun provideTemplateContentService(
		templateRegistry: TemplateRegistry
	): TemplateContentService {
		return TemplateContentServiceImpl(templateRegistry)
	}

	private fun registerTemplateFactories(registry: TemplateRegistry) {
		registry.registerListFactory("basic", BasicListTemplateFactory())
		registry.registerItemFactory("shopcard", ShopCardItemTemplateFactory())
	}
}
