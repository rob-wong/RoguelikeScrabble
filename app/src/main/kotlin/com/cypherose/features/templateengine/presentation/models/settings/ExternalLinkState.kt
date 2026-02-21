package com.cypherose.features.templateengine.presentation.models.settings

import com.cypherose.business.interfaces.State

sealed class ExternalLinkState : State {
	data class Content(
		val label: String,
		val status: String
	) : ExternalLinkState()

	data object None : ExternalLinkState()
}
