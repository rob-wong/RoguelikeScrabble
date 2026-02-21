package com.cypherose.features.homeScreen.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.buttons.presentation.ImageButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.features.homeScreen.presentation.models.HomeScreenState

class HomeScreenProvider : PreviewParameterProvider<HomeScreenState.Content> {
	override val values = sequenceOf(
		HomeScreenState.Content(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceState.Content(
					amount = "30",
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceState.None
			),
			shopButton = IconButtonState.Content(
				onClick = { },
				image = ImageState.ShopIcon,
			),
			upgradeButton = IconButtonState.Content(
				onClick = { },
				image = ImageState.UpgradeIcon,
			),
			playButton = ImageButtonState.Content(
				onClick = { },
				background = ImageState.RectangularButtonBackground,
				foreground = ImageState.PlayTextIcon,
			)
		)
	)
}
