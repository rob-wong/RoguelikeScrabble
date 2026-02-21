package com.cypherose.features.templateengine.presentation.models.settings

import com.cypherose.business.interfaces.State

sealed class AppInfoItemState : State {
	data class Content(
		val label: String,
		val value: String
	) : AppInfoItemState()

	data object None : AppInfoItemState()
}
