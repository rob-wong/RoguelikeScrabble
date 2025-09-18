package com.example.gymapprefactor.features.navigation.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
sealed class NavigationAction: Action {

    data class SetCallbacks(
        val onBack: () -> Unit
    ) : NavigationAction()

    data class GoTo(
        val navigationPage: NavigationPage
    ) : NavigationAction()

    data object GoBack : NavigationAction()

    data object None : NavigationAction()
}
