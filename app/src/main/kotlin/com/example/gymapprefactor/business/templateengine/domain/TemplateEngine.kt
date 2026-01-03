package com.example.gymapprefactor.business.templateengine.domain

import com.example.gymapprefactor.business.templateengine.data.models.ItemNet
import com.example.gymapprefactor.business.templateengine.data.models.ListNet
import com.example.gymapprefactor.business.templateengine.data.models.TemplateDataNet
import com.example.gymapprefactor.business.templateengine.domain.registry.TemplateRegistry
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateEngine @Inject constructor(
	private val templateRegistry: TemplateRegistry
) {
	private val json = Json {
		serializersModule = SerializersModule {
			polymorphic(TemplateDataNet::class) {
				subclass(ListNet::class)
				subclass(ItemNet::class)
			}
		}
		classDiscriminator = "type"
		ignoreUnknownKeys = true
		encodeDefaults = true
		useArrayPolymorphism = false
	}

	suspend fun parseAndCreateInstances(jsonString: String): List<TemplateInstance> {
		val templateDataNet = json.decodeFromString<TemplateDataNetWrapper>(jsonString)
		
		return templateDataNet.data.flatMap { templateData ->
			processTemplateData(templateData)
		}
	}

	private suspend fun processTemplateData(templateData: TemplateDataNet): List<TemplateInstance> {
		return when (templateData) {
			is ListNet -> {
				val factory = templateRegistry.getListFactory(templateData.templateId)
					?: throw IllegalArgumentException("No factory found for list template_id: ${templateData.templateId}")
				val viewModel = factory.createViewModel(templateData)
				val listInstance = TemplateInstance(
					id = templateData.id,
					templateId = templateData.templateId,
					type = "list",
					viewModel = viewModel
				)
				// Process nested includes recursively
				val nestedInstances = templateData.includes.flatMap { include ->
					processTemplateData(include)
				}
				listOf(listInstance) + nestedInstances
			}
			is ItemNet -> {
				val factory = templateRegistry.getItemFactory(templateData.templateId)
					?: throw IllegalArgumentException("No factory found for item template_id: ${templateData.templateId}")
				val viewModel = factory.createViewModel(templateData)
				listOf(
					TemplateInstance(
						id = templateData.id,
						templateId = templateData.templateId,
						type = "item",
						viewModel = viewModel
					)
				)
			}
		}
	}
}

@Serializable
private data class TemplateDataNetWrapper(
	val data: List<@Serializable TemplateDataNet>
)
