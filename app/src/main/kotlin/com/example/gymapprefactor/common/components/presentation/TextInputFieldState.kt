package com.example.gymapprefactor.common.components.presentation

sealed class TextInputFieldState {
    data class Content(
        val text: String,
        val onValueChanged: (String) -> Unit,
        val placeholder: String,
        val maxLines: Int = 1,
    ) : TextInputFieldState()

    data object None : TextInputFieldState()
}
