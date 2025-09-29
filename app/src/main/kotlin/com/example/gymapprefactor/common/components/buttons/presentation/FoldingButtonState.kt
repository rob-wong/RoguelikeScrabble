package com.example.gymapprefactor.common.components.presentation

sealed class FoldingButtonState : ButtonState {
    data class Content(
        val text: String,
        val buttons: List<RectangleButtonState.Content>,
        val buttonWidthPercentage: Float = UNTESTED_WORKS_FOR_EMULATOR_PHONE_ALSO,
    ): FoldingButtonState()

    data object None : FoldingButtonState()

    companion object {
        const val UNTESTED_WORKS_FOR_EMULATOR_PHONE = 0.33f
        const val UNTESTED_WORKS_FOR_EMULATOR_PHONE_ALSO = 0.25f
    }
}
