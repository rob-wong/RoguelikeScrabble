package com.example.gymapprefactor.features.homeScreen.presentation.state

import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class HomeScreenReducerImpl : HomeScreenReducer {
    override val state = MutableStateFlow<HomeScreenState>(HomeScreenState.None)

    override suspend fun update(action: HomeScreenAction) {
        when(action) {
            is HomeScreenAction.SetContent -> setContent(action)
            is HomeScreenAction.None -> Unit
        }
    }

    private fun setContent(action: HomeScreenAction.SetContent) {
        state.value = with(action) {
            HomeScreenState.Content(
                resourceBar = mapResourceBar(action),
                onRoutines = onRoutines,
                onExercises = onExercises,
                onViewStats = onViewStats,
                topBarState = topBarState,
                shopButton = mapShopButton(action)
            )
        }
    }

    private fun mapResourceBar(action: HomeScreenAction.SetContent): ResourceBarState {
        return ResourceBarState.Content(
	        runeState = ResourceBarState.ResourceState(
                isDisplayed = true,
                amount = action.runesCount.toString(),
                icon = ImageState.RuneIcon
            ),
	        glyphState = ResourceBarState.ResourceState(
                isDisplayed = false,
                amount = "0",
                icon = ImageState.None
            )
        )
    }

    private fun mapShopButton(action: HomeScreenAction.SetContent): ButtonState {
        return IconButtonState.Content(
	        onClick = action.navigateToShop,
	        image = ImageState.ShopIcon,
        )
    }
}
