package com.cypherose.business.templateengine.domain

import com.cypherose.business.templateengine.domain.models.TemplateContent

data class TemplateInstance(
	val id: String,
	val templateId: String,
	val type: String,
	val content: TemplateContent
)
