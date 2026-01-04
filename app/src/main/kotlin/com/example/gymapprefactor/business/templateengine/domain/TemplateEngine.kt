package com.example.gymapprefactor.business.templateengine.domain

import com.example.gymapprefactor.business.templateengine.data.models.ItemNet
import com.example.gymapprefactor.business.templateengine.data.models.ListNet
import com.example.gymapprefactor.business.templateengine.data.models.TemplateDataNet
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateItem
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateList
import com.example.gymapprefactor.business.templateengine.domain.models.TemplatePrice
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateProduct
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateEngine @Inject constructor() {
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
				// Process nested includes recursively
				val nestedContents = templateData.includes.map { include ->
					processTemplateData(include).first().content
				}
				val templateList = TemplateList(
					id = templateData.id,
					templateId = templateData.templateId,
					items = nestedContents
				)
				val listInstance = TemplateInstance(
					id = templateData.id,
					templateId = templateData.templateId,
					type = "list",
					content = templateList
				)
				// Process nested includes recursively for flat list
				val nestedInstances = templateData.includes.flatMap { include ->
					processTemplateData(include)
				}
				listOf(listInstance) + nestedInstances
			}
			is ItemNet -> {
				val templateItem = TemplateItem(
					id = templateData.id,
					templateId = templateData.templateId,
					price = templateData.price?.let { 
						TemplatePrice(type = it.type, amount = it.amount) 
					},
					product = templateData.product?.let {
						TemplateProduct(
							type = it.type,
							amount = it.amount,
							decktype = it.decktype
						)
					},
					description = templateData.description
				)
				listOf(
					TemplateInstance(
						id = templateData.id,
						templateId = templateData.templateId,
						type = "item",
						content = templateItem
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
