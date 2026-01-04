package com.example.gymapprefactor.features.templateengine.presentation.registry

import com.example.gymapprefactor.features.templateengine.presentation.factories.ItemTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.factories.ListTemplateFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateRegistryImpl @Inject constructor() : TemplateRegistry {
	private val listFactories = mutableMapOf<String, ListTemplateFactory>()
	private val itemFactories = mutableMapOf<String, ItemTemplateFactory>()

	override fun registerListFactory(templateId: String, factory: ListTemplateFactory) {
		listFactories[templateId] = factory
	}

	override fun registerItemFactory(templateId: String, factory: ItemTemplateFactory) {
		itemFactories[templateId] = factory
	}

	override fun getListFactory(templateId: String): ListTemplateFactory? {
		return listFactories[templateId]
	}

	override fun getItemFactory(templateId: String): ItemTemplateFactory? {
		return itemFactories[templateId]
	}
}
