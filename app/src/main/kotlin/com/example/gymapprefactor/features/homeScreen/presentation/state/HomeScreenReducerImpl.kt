package com.example.gymapprefactor.features.homeScreen.presentation.state

import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
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
                shopButton = mapShopButton(action),
                upgradeButton = mapUpgradeButton(action)
            )
        }
    }

    private fun mapResourceBar(action: HomeScreenAction.SetContent): ResourceBarState {
        return ResourceBarState.Content(
	        runeState = ResourceState.Content(
                amount = action.runesCount.toString(),
                icon = ImageState.RuneIcon
            ),
	        glyphState = ResourceState.None
        )
    }

    private fun mapShopButton(action: HomeScreenAction.SetContent): ButtonState {
        return IconButtonState.Content(
	        onClick = action.navigateToShop,
	        image = ImageState.ShopIcon,
        )
    }

    private fun mapUpgradeButton(action: HomeScreenAction.SetContent): ButtonState {
        return IconButtonState.Content(
            onClick = action.navigateToUpgrade,
            image = ImageState.UpgradeIcon
        )
    }
}
