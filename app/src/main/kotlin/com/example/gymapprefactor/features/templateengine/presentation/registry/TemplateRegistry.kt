package com.example.gymapprefactor.features.templateengine.presentation.registry

import com.example.gymapprefactor.features.templateengine.presentation.factories.ItemTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.factories.ListTemplateFactory

interface TemplateRegistry {
	fun registerListFactory(templateId: String, factory: ListTemplateFactory)
	fun registerItemFactory(templateId: String, factory: ItemTemplateFactory)
	fun getListFactory(templateId: String): ListTemplateFactory?
	fun getItemFactory(templateId: String): ItemTemplateFactory?
}
