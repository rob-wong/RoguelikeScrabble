package com.cypherose.business.gameplayLoop.domain.models

import com.cypherose.business.models.ActiveGameState
import com.cypherose.features.game.presentation.models.midshop.MidshopResultPayload

data class MidshopOptionResult(
	val gameState: ActiveGameState,
	val resultPayload: MidshopResultPayload?
)
