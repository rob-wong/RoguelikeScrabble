package com.example.gymapprefactor.common.components.presentation

sealed class ImageState {
	data object ResourceBarBackground : ImageState()
	data object SettingsButton : ImageState()
	data object DialogBackground : ImageState()

	data object RectangularButtonBackground : ImageState()
	data object PlayTextIcon : ImageState()
	data object BackIcon : ImageState()
	data object QuitIcon : ImageState()
	data object ConfirmIcon : ImageState()
	data object DismissIcon : ImageState()
	data object RuneIcon : ImageState()
	data object GlyphIcon : ImageState()
	data object ShopIcon : ImageState()
	data object UpgradeIcon : ImageState()
	data object None : ImageState()
}
