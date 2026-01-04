package com.example.gymapprefactor.business.templateengine.domain.models

data class TemplateItem(
	val id: String,
	val templateId: String,
	val price: TemplatePrice? = null,
	val product: TemplateProduct? = null,
	val description: String? = null
) : TemplateContent
