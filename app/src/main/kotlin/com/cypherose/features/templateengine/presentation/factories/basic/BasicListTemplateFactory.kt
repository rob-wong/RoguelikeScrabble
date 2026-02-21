package com.cypherose.features.templateengine.presentation.factories.basic

import androidx.lifecycle.ViewModel
import com.cypherose.business.templateengine.domain.models.TemplateList
import com.cypherose.features.templateengine.presentation.factories.ListTemplateFactory
import com.cypherose.features.templateengine.presentation.state.basic.BasicListReducerImpl
import com.cypherose.features.templateengine.presentation.viewmodel.basic.BasicListViewModelImpl

class BasicListTemplateFactory : ListTemplateFactory {
	override fun createViewModel(templateList: TemplateList): ViewModel {
		val reducer = BasicListReducerImpl()
		val viewModel = BasicListViewModelImpl(reducer)
		return viewModel
	}
}
