package com.cypherose.business.gameplayLoop.domain.interceptors

import com.cypherose.business.gameplayLoop.domain.ProcessedWordResult
import com.cypherose.business.gameplayLoop.domain.ScoredWordResult
import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.Letter

data class PlayWordRequest(
	val letters: List<Letter>,
	val game: ActiveGameState
)

interface PlayWordInterceptor {
	val priority: Int

	suspend fun onRequest(request: PlayWordRequest): PlayWordRequest = request

	suspend fun onResponse(
		result: ScoredWordResult,
		processed: ProcessedWordResult
	): ProcessedWordResult = processed
}
