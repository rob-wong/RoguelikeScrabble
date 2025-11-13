package com.example.gymapprefactor.features.homeScreen.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class HomeScreenAction: Action {
    data class SetContent(
        val navigateToUpgrade: () -> Unit,
        val navigateToShop: () -> Unit,
        val navigateToGame: () -> Unit,
        val runesCount: Int,
    ) : HomeScreenAction()

    data object None : HomeScreenAction()
}
