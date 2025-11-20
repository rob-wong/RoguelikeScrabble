package com.example.gymapprefactor.business.gameplayLoop.domain.models

import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopResultPayload

data class MidshopOptionResult(
	val gameState: ActiveGameState,
	val resultPayload: MidshopResultPayload?
)
