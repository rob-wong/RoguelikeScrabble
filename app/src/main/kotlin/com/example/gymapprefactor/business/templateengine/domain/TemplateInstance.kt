package com.example.gymapprefactor.business.templateengine.domain

import com.example.gymapprefactor.business.templateengine.domain.models.TemplateContent

data class TemplateInstance(
	val id: String,
	val templateId: String,
	val type: String,
	val content: TemplateContent
)
