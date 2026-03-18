package com.cypherose.common.components.presentation

sealed class ImageState {
	sealed class NinePatch : ImageState() {
		data object ResourceBarBackground : NinePatch()
		data object DialogBackground : NinePatch()
	}

	sealed class Basic : ImageState() {
		data object SettingsButton : Basic()
		data object UpgradeButton : Basic()
		data object DialogBackground : Basic()

		data object RectangularButtonBackground : Basic()
		data object PlayTextIcon : Basic()
		data object BackIcon : Basic()
		data object QuitIcon : Basic()
		data object ConfirmIcon : Basic()
		data object DismissIcon : Basic()
		data object DiscardIcon : Basic()
		data object RuneIcon : Basic()
		data object GlyphIcon : Basic()
		data object ShopIcon : Basic()
		data object UpgradeIcon : Basic()
		data object BasicBagIcon : Basic()
		data object PlaysLeftIcon : Basic()
		data object DiscardsLeftIcon : Basic()
		data object GameOverText : Basic()

		data object DefaultLetterBackground : Basic()

		data object AwakenMidshopCard : Basic()
		data object ExpungeMidshopCard : Basic()
		data object PerfectionismMidshopCard : Basic()
		data object PersistenceMidshopCard : Basic()
		data object UpgradeMidshopCard : Basic()

		data object RuneShopCard : Basic()
		data object GlyphShopCard : Basic()
		data object CrateShopCard : Basic()

		data object ComingSoon : Basic()
	}

	data object None : ImageState()
}
