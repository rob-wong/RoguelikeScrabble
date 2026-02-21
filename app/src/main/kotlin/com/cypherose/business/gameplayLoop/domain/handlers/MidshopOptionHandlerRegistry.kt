package com.cypherose.business.gameplayLoop.domain.handlers

import com.cypherose.features.game.presentation.models.midshop.MidshopOptionType
import javax.inject.Inject

class MidshopOptionHandlerRegistry @Inject constructor(
	private val upgradeHandler: UpgradeOptionHandler,
	private val awakenHandler: AwakenOptionHandler,
	private val expungeHandler: ExpungeOptionHandler,
	private val perfectionismHandler: PerfectionismOptionHandler,
	private val persistenceHandler: PersistenceOptionHandler
) {
	fun getHandler(optionType: MidshopOptionType): MidshopOptionHandler? {
		return when (optionType) {
			is MidshopOptionType.Upgrade -> upgradeHandler
			is MidshopOptionType.Awaken -> awakenHandler
			is MidshopOptionType.Expunge -> expungeHandler
			is MidshopOptionType.Perfectionism -> perfectionismHandler
			is MidshopOptionType.Persistence -> persistenceHandler
		}
	}
}

