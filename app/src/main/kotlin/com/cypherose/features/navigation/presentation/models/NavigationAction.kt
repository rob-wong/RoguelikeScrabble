package com.cypherose.features.navigation.presentation.models

import com.cypherose.business.interfaces.Action
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
