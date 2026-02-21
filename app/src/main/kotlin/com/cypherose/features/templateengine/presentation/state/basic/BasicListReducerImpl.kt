package com.cypherose.features.templateengine.presentation.state.basic

import com.cypherose.features.templateengine.presentation.models.basic.BasicListAction
import com.cypherose.features.templateengine.presentation.models.basic.BasicListState
import kotlinx.coroutines.flow.MutableStateFlow

class BasicListReducerImpl : BasicListReducer {
	override val state = MutableStateFlow<BasicListState>(BasicListState.None)

	override suspend fun update(action: BasicListAction) {
		state.value = when (action) {
			is BasicListAction.SetContent -> BasicListState.Content(
				id = action.id,
				items = action.items
			)
			is BasicListAction.None -> BasicListState.None
		}
	}
}
