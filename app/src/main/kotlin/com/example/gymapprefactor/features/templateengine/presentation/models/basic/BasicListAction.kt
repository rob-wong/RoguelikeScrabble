package com.example.gymapprefactor.features.templateengine.presentation.models.basic

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.interfaces.State

sealed class BasicListAction : Action {
	data class SetContent(
		val id: String,
		val items: List<State>
	) : BasicListAction()

	data object None : BasicListAction()
}
