package com.cypherose.features.templateengine.presentation.factories

import androidx.lifecycle.ViewModel
import com.cypherose.business.templateengine.domain.models.TemplateList

interface ListTemplateFactory {
	fun createViewModel(templateList: TemplateList): ViewModel
}

