package com.cypherose.features.homeScreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cypherose.app.util.dispatcher.DispatcherProvider
import com.cypherose.business.gameplayLoop.domain.GameplayBusinessMediator
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.user.domain.UserBusinessMediator
import com.cypherose.features.dialogs.presentation.models.DialogAction
import com.cypherose.features.dialogs.presentation.state.DialogReducer
import com.cypherose.features.homeScreen.presentation.models.HomeScreenAction
import com.cypherose.features.homeScreen.presentation.state.HomeScreenReducer
import com.cypherose.features.navigation.presentation.models.NavigationAction
import com.cypherose.features.navigation.presentation.models.NavigationPage
import com.cypherose.features.navigation.presentation.state.NavigationReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModelImpl @Inject constructor(
    private val homeScreenReducer: HomeScreenReducer,
    private val navigationReducer: NavigationReducer,
    private val dispatcherProvider: DispatcherProvider,
    private val userBusinessMediator: UserBusinessMediator,
    private val gameplayBusinessMediator: GameplayBusinessMediator,
    private val dialogReducer: DialogReducer,
) : HomeScreenViewModel() {
    override val state = homeScreenReducer.state

    init {
        startObservingUserUpdates()
        loadInitialUserContent()
        checkAndShowResumeDialog()
    }

    private fun startObservingUserUpdates() {
        viewModelScope.launch(dispatcherProvider.main) {
            userBusinessMediator.getUserFlow().collect { user ->
                handleUserUpdate(user)
            }
        }
    }

    private suspend fun handleUserUpdate(user: com.cypherose.business.models.User?) {
        if (user != null) {
            updateContentWithUser(user)
        }
    }

    private fun loadInitialUserContent() {
        viewModelScope.launch(dispatcherProvider.default) {
            userBusinessMediator.getUser().fold(
                onSuccess = { user ->
                    updateContentWithUser(user)
                },
                onFailure = { error ->
                    Timber.e(error, "Failed to load user for home screen")
                }
            )
        }
    }

    private suspend fun updateContentWithUser(user: com.cypherose.business.models.User) {
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

    private fun checkAndShowResumeDialog() {
        viewModelScope.launch(dispatcherProvider.default) {
            val activeGame = gameplayBusinessMediator.getActiveGameIfExists()
            if (activeGame != null) {
                showResumeDialog(activeGame)
            }
        }
    }

    private suspend fun showResumeDialog(activeGame: ActiveGameState) {
        val earnedRunes = activeGame.activeGameVariables.runesCount - activeGame.activeGameVariables.startingRunesCount
        val message = if (earnedRunes > 0) {
            "Runes earned: $earnedRunes"
        } else {
            null
        }

        withContext(dispatcherProvider.main) {
            dialogReducer.update(
                DialogAction.TriggerDialog(
                    onDismiss = { onResumeDialogEnd(activeGame) },
                    title = "Resume Game?",
                    message = message,
                    showDismissButton = true,
                    confirmState = DialogAction.ConfirmState.Content(
                        onConfirm = { onResumeDialogContinue() }
                    )
                )
            )
        }
    }

    private fun onResumeDialogContinue() {
        navigateToGame()
        viewModelScope.launch(dispatcherProvider.main) {
            dialogReducer.update(DialogAction.ClearDialogs)
        }
    }

    private fun onResumeDialogEnd(activeGame: ActiveGameState) {
        viewModelScope.launch(dispatcherProvider.default) {
            gameplayBusinessMediator.endGame(activeGame, saveProgression = false)
            withContext(dispatcherProvider.main) {
                dialogReducer.update(DialogAction.ClearDialogs)
            }
        }
    }
}
