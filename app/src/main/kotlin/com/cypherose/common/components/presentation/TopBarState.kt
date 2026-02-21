package com.cypherose.common.components.presentation

sealed class TopBarState {
    data class Content(
        val title: String,
        val onBack: () -> Unit,
    ) : TopBarState()

    data object None : TopBarState()
}
