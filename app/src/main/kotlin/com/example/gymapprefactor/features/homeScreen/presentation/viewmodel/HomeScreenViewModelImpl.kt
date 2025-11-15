package com.example.gymapprefactor.features.homeScreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.state.HomeScreenReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModelImpl @Inject constructor(
    private val homeScreenReducer: HomeScreenReducer,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
) : HomeScreenViewModel() {
    override val state = homeScreenReducer.state

    init {
        setContent()
    }

    private fun setContent() {
        viewModelScope.launch(dispatcherProvider.default) {
            homeScreenReducer.update(HomeScreenAction.SetContent(
                runesCount = 30, // TODO use a data model
                navigateToShop = ::navigateToShop,
                navigateToUpgrade = ::navigateToUpgrade,
                navigateToGame = ::navigateToGame,
            ))
        }
    }

    private fun navigateToShop() = navigateToScreen(NavigationPage.ShopScreen)

    private fun navigateToUpgrade() = navigateToScreen(NavigationPage.UpgradeScreen)

    private fun navigateToGame() = navigateToScreen(NavigationPage.GameScreen)

    private fun navigateToScreen(navigationPage: NavigationPage) {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoTo(navigationPage))
        }
    }
}
