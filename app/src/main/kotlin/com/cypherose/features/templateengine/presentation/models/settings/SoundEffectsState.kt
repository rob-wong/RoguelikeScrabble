package com.cypherose.features.templateengine.presentation.models.settings

import com.cypherose.business.interfaces.State

sealed class SoundEffectsState : State {
	data class Content(
		val volume: Float,
		val volumeLabel: String,
		val placeholderText: String
	) : SoundEffectsState()

	data object None : SoundEffectsState()
}
