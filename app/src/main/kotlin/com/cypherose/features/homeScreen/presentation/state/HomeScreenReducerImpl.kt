package com.cypherose.features.homeScreen.presentation.state

import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.buttons.presentation.ImageButtonState
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.features.homeScreen.presentation.models.HomeScreenAction
import com.cypherose.features.homeScreen.presentation.models.HomeScreenState
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
                shopButton = mapShopButton(action),
                upgradeButton = mapUpgradeButton(action),
                playButton = mapPlayButton(action)
            )
        }
    }

    private fun mapResourceBar(action: HomeScreenAction.SetContent): ResourceBarState {
        return ResourceBarState.Content(
	        runeState = ResourceState.Content(
                amount = action.runesCount.toString(),
                icon = ImageState.Basic.RuneIcon
            ),
	        glyphState = ResourceState.None
        )
    }

    private fun mapShopButton(action: HomeScreenAction.SetContent): ButtonState {
        return IconButtonState.Content(
	        onClick = action.navigateToShop,
	        image = ImageState.Basic.ShopIcon,
        )
    }

    private fun mapUpgradeButton(action: HomeScreenAction.SetContent): ButtonState {
        return IconButtonState.Content(
            onClick = action.navigateToUpgrade,
            image = ImageState.Basic.UpgradeIcon
        )
    }

    private fun mapPlayButton(action: HomeScreenAction.SetContent): ButtonState {
        return ImageButtonState.Content(
	        onClick = action.navigateToGame,
	        background = ImageState.Basic.RectangularButtonBackground,
	        foreground = ImageState.Basic.PlayTextIcon,
        )
    }
}
