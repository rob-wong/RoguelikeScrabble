package com.cypherose.common.components.presentation

sealed class ResourceBarState {
	data class Content(
		val runeState: ResourceState,
		val glyphState: ResourceState,
	) : ResourceBarState()

	data object None : ResourceBarState()
}

sealed class ResourceState {
	data class Content(
		val amount: String,
		val icon: ImageState,
	) : ResourceState()

	data object None : ResourceState()
}
