package com.example.gymapprefactor.business.templateengine.domain

import androidx.lifecycle.ViewModel

data class TemplateInstance(
	val id: String,
	val templateId: String,
	val type: String,
	val viewModel: ViewModel
)
