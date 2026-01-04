package com.example.gymapprefactor.business.templateengine.domain.models

data class TemplateProduct(
	val type: String,
	val amount: Int? = null,
	val decktype: String? = null
)
