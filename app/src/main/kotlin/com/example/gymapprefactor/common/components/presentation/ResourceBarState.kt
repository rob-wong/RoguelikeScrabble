package com.example.gymapprefactor.common.components.presentation

sealed class ResourceBarState {
	data class Content(
		val runeState: ResourceState,
		val glyphState: ResourceState,
	) : ResourceBarState()

	data object None : ResourceBarState()

	data class ResourceState(
		val isDisplayed: Boolean,
		val amount: String,
		val icon: ImageState,
	)
}
