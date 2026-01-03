package com.example.gymapprefactor.business.templateengine.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

@Polymorphic
@Serializable
sealed interface TemplateDataNet

@Serializable
@SerialName("list")
data class ListNet(
	@SerialName("template_id")
	val templateId: String,
	val id: String,
	val includes: List<TemplateDataNet> = emptyList()
) : TemplateDataNet

@Serializable
@SerialName("item")
data class ItemNet(
	@SerialName("template_id")
	val templateId: String,
	val id: String,
	val price: PriceNet? = null,
	val product: ProductNet? = null,
	val description: String? = null
) : TemplateDataNet
