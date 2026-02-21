package com.cypherose.features.navigation.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cypherose.features.game.ui.GameRoot
import com.cypherose.features.homeScreen.ui.HomeScreenRouter
import com.cypherose.features.navigation.presentation.models.NavigationPage.*
import com.cypherose.features.navigation.presentation.models.NavigationState
import com.cypherose.features.navigation.presentation.viewmodel.NavigationViewModelImpl
import com.cypherose.features.shop.ui.ShopScreen
import com.cypherose.features.upgrade.ui.UpgradeScreen

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModelImpl = hiltViewModel(),
) {

    BackHandler {
        viewModel.onBackPressed()
    }

    val screenState: NavigationState by viewModel.state.collectAsStateWithLifecycle(
        NavigationState.None
    )

    Box(modifier = modifier) {
        when(val state = screenState) {
            is NavigationState.CurrentPage -> NavigationPageRouter(state)
            is NavigationState.None -> Unit
        }
    }
}

@Composable
fun NavigationPageRouter(
    state: NavigationState.CurrentPage
) {
    when(state.page) {
        is HomeScreen -> HomeScreenRouter()
        is ShopScreen -> ShopScreen()
        is UpgradeScreen -> UpgradeScreen()
        is GameScreen -> GameRoot()
    }
}
