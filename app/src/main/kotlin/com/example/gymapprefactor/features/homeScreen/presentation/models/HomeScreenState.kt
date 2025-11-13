package com.example.gymapprefactor.features.homeScreen.presentation.models

import com.example.gymapprefactor.business.interfaces.State
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState

sealed class HomeScreenState: State {

    data class Content (
        val shopButton: ButtonState,
        val upgradeButton: ButtonState,
        val playButton: ButtonState,
        val resourceBar: ResourceBarState,
    ): HomeScreenState()

    data object None : HomeScreenState()
}
