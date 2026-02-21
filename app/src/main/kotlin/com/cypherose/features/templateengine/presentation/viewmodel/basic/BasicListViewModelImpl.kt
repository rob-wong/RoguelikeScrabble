package com.cypherose.features.templateengine.presentation.viewmodel.basic

import com.cypherose.business.interfaces.State
import com.cypherose.features.templateengine.presentation.models.basic.BasicListAction
import com.cypherose.features.templateengine.presentation.state.basic.BasicListReducer

class BasicListViewModelImpl(
	private val basicListReducer: BasicListReducer
) : BasicListViewModel() {
	override val state = basicListReducer.state

	override suspend fun setContent(id: String, items: List<State>) {
		basicListReducer.update(BasicListAction.SetContent(id = id, items = items))
	}
}
