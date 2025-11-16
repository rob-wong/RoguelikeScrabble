package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.effects.domain.EffectsRepository
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessorFactory
import com.example.gymapprefactor.business.effects.templating.domain.MultiplicationConfig
import com.example.gymapprefactor.business.models.Effect
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject

data class EffectScoreModification(
	val effectId: String,
	val effectLabel: String,
	val scoreDelta: Int,
	val orderIndex: Int,
	val multiplier: Double? = null // For multiplication effects, the multiplier to display (e.g., 2.0 for "x 2")
)

interface EffectScoreMapper {
	data class Param(
		val effects: List<Effect>,
		val rawScore: Int
	)
	
	suspend fun map(param: Param): List<EffectScoreModification>
}

class EffectScoreMapperImpl @Inject constructor(
	private val effectsRepository: EffectsRepository,
	private val processorFactory: EffectProcessorFactory,
	private val json: Json
) : EffectScoreMapper {

	override suspend fun map(param: EffectScoreMapper.Param): List<EffectScoreModification> {
		val descriptorMap = effectsRepository.getEffectDescriptors().first()
		val oldEffectMap = effectsRepository.getEffects().first()
		
		var currentScore = param.rawScore
		
		return param.effects.mapIndexed { index, effect ->
			val descriptor = effect.descriptor ?: descriptorMap[effect.label]
			val scoreDelta = calculateScoreDelta(
				effect = effect,
				currentScore = currentScore,
				nextEffect = param.effects.getOrNull(index + 1),
				descriptorMap = descriptorMap,
				oldEffectMap = oldEffectMap
			)
			
			// Extract multiplier for display if this is a multiplication effect
			val multiplier = extractMultiplier(descriptor)
			
			currentScore += scoreDelta
			
			EffectScoreModification(
				effectId = effect.id,
				effectLabel = effect.label,
				scoreDelta = scoreDelta,
				orderIndex = index,
				multiplier = multiplier
			)
		}
	}

	private fun calculateScoreDelta(
		effect: Effect,
		currentScore: Int,
		nextEffect: Effect?,
		descriptorMap: Map<String, EffectDescriptor>,
		oldEffectMap: Map<String, Int>
	): Int {
		// Try to use descriptor from effect first
		val descriptor = effect.descriptor
			?: descriptorMap[effect.label]

		if (descriptor != null) {
			val processor = processorFactory.createProcessor(descriptor.type)
			if (processor != null) {
				return processor.calculate(currentScore, descriptor, nextEffect)
			}
		}

		// Fallback to old behavior for backward compatibility
		return oldEffectMap[effect.label] ?: effect.label.length
	}

	/**
	 * Extracts the multiplier from a multiplication effect descriptor.
	 * Also handles nested multiplication effects in combos.
	 */
	private fun extractMultiplier(descriptor: EffectDescriptor?): Double? {
		if (descriptor == null) return null
		
		return when (descriptor.type) {
			"multiplication" -> {
				try {
					val config = json.decodeFromJsonElement(
						serializer<MultiplicationConfig>(),
						descriptor.config
					)
					config.multiplier
				} catch (_ : Exception) {
					null
				}
			}
			"combo" -> {
				// For combos, we'd need to check which effect is being applied
				// For now, return null - combos will show the delta
				// TODO: If needed, we could extract multiplier from combo's nested effects
				null
			}
			else -> null
		}
	}
}
