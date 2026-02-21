package com.cypherose.common.components.buttons.presentation

sealed class RectangleButtonState : ButtonState {
    data class Content(
        val onClick: () -> Unit,
        val text: String,
    ) : RectangleButtonState()

    data object None : RectangleButtonState()
}
