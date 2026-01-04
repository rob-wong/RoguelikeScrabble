package com.example.gymapprefactor.features.templateengine.presentation.factories

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateItem

interface ItemTemplateFactory {
	fun createViewModel(templateItem: TemplateItem): ViewModel
}

