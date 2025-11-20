package com.example.gymapprefactor.business.gameplayLoop.domain.handlers

import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOptionType
import javax.inject.Inject

class MidshopOptionHandlerRegistry @Inject constructor(
	private val upgradeHandler: UpgradeOptionHandler,
	private val awakenHandler: AwakenOptionHandler,
	private val expungeHandler: ExpungeOptionHandler
) {
	fun getHandler(optionType: MidshopOptionType): MidshopOptionHandler? {
		return when (optionType) {
			is MidshopOptionType.Upgrade -> upgradeHandler
			is MidshopOptionType.Awaken -> awakenHandler
			is MidshopOptionType.Expunge -> expungeHandler
			is MidshopOptionType.Perfectionism -> null // TODO: Implement in future PR
			is MidshopOptionType.Persistence -> null // TODO: Implement in future PR
		}
	}
}

