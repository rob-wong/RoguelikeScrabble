package com.cypherose.features.navigation.presentation.models

import com.cypherose.business.interfaces.State

sealed class NavigationState: State {
    data class CurrentPage(val page: NavigationPage) : NavigationState()

    data object None : NavigationState()
}
