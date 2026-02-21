package com.cypherose.features.templateengine.presentation.models.basic

import com.cypherose.business.interfaces.Action
import com.cypherose.business.interfaces.State

sealed class BasicListAction : Action {
	data class SetContent(
		val id: String,
		val items: List<State>
	) : BasicListAction()

	data object None : BasicListAction()
}
