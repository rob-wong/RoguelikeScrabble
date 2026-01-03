package com.example.gymapprefactor.features.templateengine.presentation.models.basic

import com.example.gymapprefactor.business.interfaces.State

sealed class BasicListState : State {
	data class Content(
		val id: String,
		val items: List<Any> // Will hold item ViewModels or states
	) : BasicListState()

	data object None : BasicListState()
}
