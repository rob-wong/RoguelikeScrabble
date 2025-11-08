package com.example.gymapprefactor.common.components.presentation

sealed class BagState {
	data class Content(
		val image: ImageState,
		val currentLetters: Int,
		val maxLetters: Int,
	) : BagState()

	data object None : BagState()
}
