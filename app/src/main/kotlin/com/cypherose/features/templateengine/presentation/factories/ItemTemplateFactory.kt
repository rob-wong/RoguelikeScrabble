package com.cypherose.features.templateengine.presentation.factories

import androidx.lifecycle.ViewModel
import com.cypherose.business.templateengine.domain.models.TemplateItem

interface ItemTemplateFactory {
	fun createViewModel(templateItem: TemplateItem): ViewModel
}

