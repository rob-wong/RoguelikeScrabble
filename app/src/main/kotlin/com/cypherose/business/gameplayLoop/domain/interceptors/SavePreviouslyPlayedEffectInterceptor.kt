package com.cypherose.business.gameplayLoop.domain.interceptors

import com.cypherose.business.effects.domain.EffectsRepository
import com.cypherose.business.gameplayLoop.domain.ProcessedWordResult
import com.cypherose.business.gameplayLoop.domain.ScoredWordResult
import com.cypherose.business.gameplayLoop.domain.usecases.AddPreviouslyPlayedEffectUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SavePreviouslyPlayedEffectInterceptor @Inject constructor(
	private val addPreviouslyPlayedEffectUseCase: AddPreviouslyPlayedEffectUseCase,
	private val effectsRepository: EffectsRepository,
) : PlayWordInterceptor {
	override val priority: Int = 1

	override suspend fun onResponse(
		result: ScoredWordResult,
		processed: ProcessedWordResult
	): ProcessedWordResult {
		val label = result.wordEffect.label
		if (!hasEffectDescriptor(label)) return processed
		addPreviouslyPlayedEffectUseCase(label)
		return appendToProcessedResult(processed, label)
	}

	private fun appendToProcessedResult(processed: ProcessedWordResult, label: String): ProcessedWordResult {
		val currentLabels = processed.gameState.previouslyPlayedEffectLabels
		if (label in currentLabels) return processed
		return processed.copy(
			gameState = processed.gameState.copy(
				previouslyPlayedEffectLabels = currentLabels + label
			)
		)
	}

	private suspend fun hasEffectDescriptor(label: String): Boolean {
		val descriptorMap = effectsRepository.getEffectDescriptors().first()
		return descriptorMap[label] != null
	}
}
