package com.cypherose.features.templateengine.presentation.models.basic

import com.cypherose.business.interfaces.State

sealed class BasicListState : State {
	data class Content(
		val id: String,
		val items: List<State>
	) : BasicListState()

	data object None : BasicListState()
}
