package com.cypherose.business.gameplayLoop.domain.interceptors

import com.cypherose.business.gameplayLoop.domain.mappers.DrawHandMapper
import com.cypherose.business.models.ActiveGameState

interface DrawHandInterceptor {
	val priority: Int

	fun onRequest(param: DrawHandMapper.Param): DrawHandMapper.Param = param

	fun onResponse(
		gameState: ActiveGameState,
		drawResult: DrawHandMapper.Output
	): ActiveGameState = gameState
}
