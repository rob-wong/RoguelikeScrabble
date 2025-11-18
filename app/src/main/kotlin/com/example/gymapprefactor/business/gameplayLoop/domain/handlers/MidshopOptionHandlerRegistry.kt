package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.features.game.presentation.models.MidshopOptionType
import javax.inject.Inject

class MidshopOptionHandlerRegistry @Inject constructor(
	private val upgradeHandler: UpgradeOptionHandler,
	private val awakenHandler: AwakenOptionHandler
) {
	fun getHandler(optionType: MidshopOptionType): MidshopOptionHandler? {
		return when (optionType) {
			is MidshopOptionType.Upgrade -> upgradeHandler
			is MidshopOptionType.Awaken -> awakenHandler
			is MidshopOptionType.Expunge -> null // TODO: Implement in future PR
			is MidshopOptionType.Perfectionism -> null // TODO: Implement in future PR
			is MidshopOptionType.Persistence -> null // TODO: Implement in future PR
		}
	}
}

