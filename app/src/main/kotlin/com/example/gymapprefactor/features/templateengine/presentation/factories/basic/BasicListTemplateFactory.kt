package com.example.gymapprefactor.features.templateengine.presentation.factories.basic

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.business.templateengine.data.models.ListNet
import com.example.gymapprefactor.business.templateengine.domain.registry.ListTemplateFactory
import com.example.gymapprefactor.features.templateengine.presentation.state.basic.BasicListReducerImpl
import com.example.gymapprefactor.features.templateengine.presentation.viewmodel.basic.BasicListViewModelImpl

class BasicListTemplateFactory : ListTemplateFactory {
	override fun createViewModel(listNet: ListNet): ViewModel {
		val reducer = BasicListReducerImpl()
		return BasicListViewModelImpl(reducer)
	}
}
