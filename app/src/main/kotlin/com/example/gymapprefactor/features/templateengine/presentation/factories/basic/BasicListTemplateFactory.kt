package com.example.gymapprefactor.features.templateengine.presentation.factories.basic

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.domain.models.TemplateList
import com.example.gymapprefactor.features.templateengine.presentation.factories.ListTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.state.basic.BasicListReducerImpl
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.basic.BasicListViewModelImpl

class BasicListTemplateFactory : ListTemplateFactory {
	override fun createViewModel(templateList: TemplateList): ViewModel {
		val reducer = BasicListReducerImpl()
		val viewModel = BasicListViewModelImpl(reducer)
		return viewModel
	}
}
