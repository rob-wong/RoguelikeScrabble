package com.cypherose.business.gameplayLoop.domain.handlers

import com.cypherose.business.gameplayLoop.domain.mappers.UpgradeMidshopOptionMapper
import com.cypherose.business.models.ActiveGameState
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopResultPayload
import javax.inject.Inject

class UpgradeOptionHandler @Inject constructor(
	private val upgradeMidshopOptionMapper: UpgradeMidshopOptionMapper
) : MidshopOptionHandler {

	override fun execute(
		option: MidshopOption,
		game: ActiveGameState
	): MidshopOptionExecutionResult {
		val upgradeResult = upgradeMidshopOptionMapper.map(
			UpgradeMidshopOptionMapper.Param(game = game)
		)
		
		val payload = MidshopResultPayload.Upgrade(
			originalLetters = upgradeResult.originalLetters,
			upgradedLetters = upgradeResult.upgradedLetters,
			glyphsGained = upgradeResult.glyphsGained
		)
		
		return MidshopOptionExecutionResult(
			gameState = upgradeResult.gameState,
			resultPayload = payload,
			shouldAdvance = true
		)
	}
}

