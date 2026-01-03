package com.example.gymapprefactor.business.templateengine.domain.registry

interface TemplateRegistry {
	fun registerListFactory(templateId: String, factory: ListTemplateFactory)
	fun registerItemFactory(templateId: String, factory: ItemTemplateFactory)
	fun getListFactory(templateId: String): ListTemplateFactory?
	fun getItemFactory(templateId: String): ItemTemplateFactory?
}
