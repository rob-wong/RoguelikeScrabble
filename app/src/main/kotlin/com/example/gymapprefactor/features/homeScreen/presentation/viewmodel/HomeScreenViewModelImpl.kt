package com.example.gymapprefactor.features.homeScreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.business.user.domain.UserBusinessMediator
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
    private val userBusinessMediator: UserBusinessMediator,
) : HomeScreenViewModel() {
    override val state = homeScreenReducer.state

    init {
        startObservingUserUpdates()
        loadInitialUserContent()
    }

    private fun startObservingUserUpdates() {
        viewModelScope.launch(dispatcherProvider.main) {
            userBusinessMediator.getUserFlow().collect { user ->
                handleUserUpdate(user)
            }
        }
    }

    private suspend fun handleUserUpdate(user: com.example.gymapprefactor.business.models.User?) {
        if (user != null) {
            updateContentWithUser(user)
        }
    }

    private fun loadInitialUserContent() {
        viewModelScope.launch(dispatcherProvider.default) {
            val user = userBusinessMediator.getUser()
            updateContentWithUser(user)
        }
    }

    private suspend fun updateContentWithUser(user: com.example.gymapprefactor.business.models.User) {
        homeScreenReducer.update(HomeScreenAction.SetContent(
            runesCount = user.runesCount,
            navigateToShop = ::navigateToShop,
            navigateToUpgrade = ::navigateToUpgrade,
            navigateToGame = ::navigateToGame,
        ))
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
