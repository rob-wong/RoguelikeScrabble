package com.example.gymapprefactor.features.templateengine.presentation.factories

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateList

interface ListTemplateFactory {
	fun createViewModel(templateList: TemplateList): ViewModel
}

