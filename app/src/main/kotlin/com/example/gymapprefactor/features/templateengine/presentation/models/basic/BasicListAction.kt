package com.example.gymapprefactor.features.templateengine.presentation.models.basic

import com.example.gymapprefactor.business.interfaces.Action

sealed class BasicListAction : Action {
	data class SetContent(
		val id: String,
		val items: List<Any>
	) : BasicListAction()

	data object None : BasicListAction()
}
