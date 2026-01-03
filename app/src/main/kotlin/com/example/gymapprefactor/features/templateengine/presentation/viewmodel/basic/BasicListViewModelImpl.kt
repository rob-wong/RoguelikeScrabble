package com.example.gymapprefactor.features.templateengine.presentation.viewmodel.basic

import com.example.gymapprefactor.features.templateengine.presentation.state.basic.BasicListReducer

class BasicListViewModelImpl(
	private val basicListReducer: BasicListReducer
) : BasicListViewModel() {
	override val state = basicListReducer.state
}
