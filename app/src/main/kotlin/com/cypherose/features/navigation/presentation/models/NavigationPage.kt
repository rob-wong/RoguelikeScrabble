package com.cypherose.features.navigation.presentation.models

sealed class NavigationPage {
    data object HomeScreen : NavigationPage()
    data object ShopScreen : NavigationPage()
    data object UpgradeScreen : NavigationPage()
    data object GameScreen : NavigationPage()
}
