package com.example.gymapprefactor.features.navigation.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.gymapprefactor.app.util.dispatcher.DispatcherProvider
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModelImpl @Inject constructor(
    private val navigationReducer: NavigationReducer,
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
        sendGoToAction(state.page)
        println(navigationStack.toString())
    }

    // instead of this, make a back event reducer that routines,
    // exercises collect, then have them send it back to an ongoback
    // TODO: make event reducer when routines has more stuff set up
    fun onBackPressed() {
        onGoBack()
    }

    private fun onGoBack() {
        navigationStack.removeAt(navigationStack.size - 1)
        sendGoToAction(navigationStack.removeAt(navigationStack.size - 1))
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
}
