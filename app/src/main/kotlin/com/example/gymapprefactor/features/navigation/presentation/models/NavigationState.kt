package com.example.gymapprefactor.features.navigation.presentation.models

import com.example.gymapprefactor.business.interfaces.State

sealed class NavigationState: State {
    data class CurrentPage(val page: NavigationPage) : NavigationState()

    data object None : NavigationState()
}
