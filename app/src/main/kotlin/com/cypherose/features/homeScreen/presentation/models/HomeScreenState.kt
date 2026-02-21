package com.cypherose.features.homeScreen.presentation.models

import com.cypherose.business.interfaces.State
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.presentation.ResourceBarState

sealed class HomeScreenState: State {

    data class Content (
        val shopButton: ButtonState,
        val upgradeButton: ButtonState,
        val playButton: ButtonState,
        val resourceBar: ResourceBarState,
    ): HomeScreenState()

    data object None : HomeScreenState()
}
