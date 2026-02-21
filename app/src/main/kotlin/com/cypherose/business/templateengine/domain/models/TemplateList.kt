package com.cypherose.business.templateengine.domain.models

data class TemplateList(
	val id: String,
	val templateId: String,
	val items: List<TemplateContent> = emptyList()
) : TemplateContent
