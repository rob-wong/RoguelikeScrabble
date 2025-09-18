package com.example.gymapprefactor.features.navigation.presentation.state

import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import kotlinx.coroutines.flow.MutableStateFlow

class NavigationReducerImpl : NavigationReducer {
    override val state = MutableStateFlow<NavigationState>(NavigationState.None)

    private lateinit var onBackCallback: () -> Unit

    override suspend fun update(action: NavigationAction) {
        when(action) {
            is NavigationAction.SetCallbacks -> setCallbacks(action)
            is NavigationAction.GoTo -> goToPage(action.navigationPage)
            is NavigationAction.GoBack -> onBackCallback()
            is NavigationAction.None -> Unit
        }
    }

    private fun goToPage(navigationPage: NavigationPage) {
        state.value = NavigationState.CurrentPage(page = navigationPage)
    }

    private fun setCallbacks(action: NavigationAction.SetCallbacks) {
        onBackCallback = action.onBack
    }
}
