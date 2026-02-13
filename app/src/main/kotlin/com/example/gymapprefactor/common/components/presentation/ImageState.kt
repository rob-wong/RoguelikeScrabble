package com.example.gymapprefactor.common.components.presentation

sealed class ImageState {
	data object ResourceBarBackground : ImageState()
	data object SettingsButton : ImageState()
	data object UpgradeButton : ImageState()
	data object DialogBackground : ImageState()

	data object RectangularButtonBackground : ImageState()
	data object PlayTextIcon : ImageState()
	data object BackIcon : ImageState()
	data object QuitIcon : ImageState()
	data object ConfirmIcon : ImageState()
	data object DismissIcon : ImageState()
	data object DiscardIcon : ImageState()
	data object RuneIcon : ImageState()
	data object GlyphIcon : ImageState()
	data object ShopIcon : ImageState()
	data object UpgradeIcon : ImageState()
	data object BasicBagIcon : ImageState()
	data object PlaysLeftIcon : ImageState()
	data object DiscardsLeftIcon : ImageState()
    data object GameOverText : ImageState()

	data object DefaultLetterBackground : ImageState()

	data object AwakenMidshopCard : ImageState()
	data object ExpungeMidshopCard : ImageState()
	data object PerfectionismMidshopCard : ImageState()
	data object PersistenceMidshopCard : ImageState()
	data object UpgradeMidshopCard : ImageState()

	data object RuneShopCard : ImageState()
	data object GlyphShopCard : ImageState()
	data object CrateShopCard : ImageState()

	data object ComingSoon : ImageState()

	data object None : ImageState()
}
