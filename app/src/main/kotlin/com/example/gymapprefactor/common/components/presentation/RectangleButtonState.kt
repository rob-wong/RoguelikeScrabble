package com.example.gymapprefactor.common.components.presentation

sealed class RectangleButtonState : ButtonState {
    data class Content(
        val onClick: () -> Unit,
        val text: String,
    ) : RectangleButtonState()

    data object None : RectangleButtonState()
}
