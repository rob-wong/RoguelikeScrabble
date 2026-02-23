package com.cypherose.business.gameplayLoop.domain.interceptors

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.gameplayLoop.domain.mappers.DrawHandMapper
import com.cypherose.business.models.ActiveGameState
import javax.inject.Inject

class RefreshPreviouslyPlayedEffectInterceptor @Inject constructor(
	private val gameplayRepository: GameplayRepository,
) : DrawHandInterceptor {
	override val priority: Int = 1

	override fun onResponse(
		gameState: ActiveGameState,
		drawResult: DrawHandMapper.Output
	): ActiveGameState {
		val labels = gameplayRepository.getPreviouslyPlayedEffects()
		return gameState.copy(previouslyPlayedEffectLabels = labels)
	}
}
