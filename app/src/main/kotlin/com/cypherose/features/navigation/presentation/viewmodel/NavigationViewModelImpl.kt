package com.cypherose.features.navigation.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.common.components.presentation.ScreenBackgroundState
import com.cypherose.common.components.presentation.models.BackgroundAction
import com.cypherose.common.components.presentation.state.BackgroundReducer
import com.cypherose.features.navigation.presentation.models.NavigationAction
import com.cypherose.features.navigation.presentation.models.NavigationPage
import com.cypherose.features.navigation.presentation.models.NavigationState
import com.cypherose.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NavigationViewModelImpl @Inject constructor(
    private val navigationReducer: NavigationReducer,
    private val backgroundReducer: BackgroundReducer,
    private val dispatcherProvider: DispatcherProvider,
) : NavigationViewModel() {

    override val state = navigationReducer.state
    private val navigationStack = mutableListOf<NavigationPage>()

    init {
        initCollection()
        initReducerCallbacks()
        goToInitialScreen()
    }

    private fun initCollection() {
        viewModelScope.launch(dispatcherProvider.default) {
            state.collect {
                when (it) {
                    is NavigationState.CurrentPage -> onPageChange(it)
                    is NavigationState.None -> Unit
                }
            }
        }
    }

    private fun initReducerCallbacks() {
        viewModelScope.launch(dispatcherProvider.default) {
            navigationReducer.update(
                NavigationAction.SetCallbacks(onBack = ::onGoBack)
            )
        }
    }

    private fun onPageChange(state: NavigationState.CurrentPage) {
        val index = findIndexForPageInStack(state.page)
        addPageToBackStack(page = state.page, newIndex = index)
        setBackgroundForPage(state.page)
        sendGoToAction(state.page)
        Timber.d("Navigation stack: $navigationStack")
    }

    // instead of this, make a back event reducer that routines,
    // exercises collect, then have them send it back to an ongoback
    // TODO: make event reducer when routines has more stuff set up
    fun onBackPressed() {
        onGoBack()
    }

    private fun onGoBack() {
        if (navigationStack.isEmpty()) {
            return
        }
        navigationStack.removeAt(navigationStack.size - 1)
        val previousPage = navigationStack.lastOrNull() ?: NavigationPage.HomeScreen
        sendGoToAction(previousPage)
    }

    private fun findIndexForPageInStack(page: NavigationPage): Int {
        navigationStack.forEachIndexed { index, navigationPage ->
            if (navigationPage == page) { return index }
        }

        return navigationStack.size
    }

    private fun addPageToBackStack(page: NavigationPage, newIndex: Int) {
        shortenBackStackToIndex(newIndex)
        navigationStack.add(page)
    }

    private fun shortenBackStackToIndex(newIndex: Int) {
        for(index in navigationStack.indices.reversed()) {
            if (index >= newIndex) { navigationStack.removeAt(index) }
        }
    }

    private fun goToInitialScreen() {
        onPageChange(NavigationState.CurrentPage(NavigationPage.HomeScreen))
    }

    private fun sendGoToAction(page: NavigationPage) {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationReducer.update(NavigationAction.GoTo(page))
        }
    }

    private fun setBackgroundForPage(page: NavigationPage) {
        val backgroundState = mapPageToBackground(page)
        viewModelScope.launch(dispatcherProvider.main) {
            backgroundReducer.update(BackgroundAction.SetBackground(backgroundState))
        }
    }

    private fun mapPageToBackground(page: NavigationPage): ScreenBackgroundState {
        return when (page) {
            is NavigationPage.HomeScreen -> ScreenBackgroundState.Home
            is NavigationPage.ShopScreen -> ScreenBackgroundState.Shop
            is NavigationPage.UpgradeScreen -> ScreenBackgroundState.Upgrade
            is NavigationPage.GameScreen -> ScreenBackgroundState.Game
        }
    }
}
