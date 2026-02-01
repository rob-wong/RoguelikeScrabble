package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.effects.domain.EffectsRepository
import com.example.gymapprefactor.business.effects.templating.domain.EffectContext
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.effects.templating.domain.EffectModificationResult
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessorFactory
import com.example.gymapprefactor.business.models.Effect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class EffectScoreModification(
	val effectId: String,
	val effectLabel: String,
	val scoreDelta: Int,
	val orderIndex: Int,
	val multiplier: Double?, // For multiplication effects, the multiplier to display (e.g., 2.0 for "x 2")
	val isChanceMultiplier: Boolean, // True if this is a chance-type multiplication effect
	val glyphAmount: Int
)

interface EffectScoreMapper {
	data class Param(
		val effects: List<Effect>,
		val rawScore: Int,
		val seed: Long,
		val level: Int,
		val round: Int
	)
	
	suspend fun map(param: Param): List<EffectScoreModification>
}

class EffectScoreMapperImpl @Inject constructor(
	private val effectsRepository: EffectsRepository,
	private val processorFactory: EffectProcessorFactory
) : EffectScoreMapper {

	override suspend fun map(param: EffectScoreMapper.Param): List<EffectScoreModification> {
		val descriptorMap = effectsRepository.getEffectDescriptors().first()
		
		var currentScore = param.rawScore
		
		return param.effects.mapIndexed { index, effect ->
			val descriptor = getEffectDescriptor(effect, descriptorMap)
			val context = EffectContext(
				seed = param.seed,
				level = param.level,
				round = param.round,
				effectIndex = index
			)
			val modificationResult = calculateModificationResult(
				effect = effect,
				currentScore = currentScore,
				nextEffect = param.effects.getOrNull(index + 1),
				descriptor = descriptor,
				context = context
			)
			
			currentScore += modificationResult.scoreDelta
			
			EffectScoreModification(
				effectId = effect.id,
				effectLabel = effect.label,
				scoreDelta = modificationResult.scoreDelta,
				orderIndex = index,
				multiplier = modificationResult.multiplier,
				isChanceMultiplier = modificationResult.isChanceMultiplier,
				glyphAmount = modificationResult.glyphAmount
			)
		}
	}

	private fun getEffectDescriptor(
		effect: Effect,
		descriptorMap: Map<String, EffectDescriptor>
	): EffectDescriptor? {
		return effect.descriptor ?: descriptorMap[effect.label]
	}

	private fun calculateModificationResult(
		effect: Effect,
		currentScore: Int,
		nextEffect: Effect?,
		descriptor: EffectDescriptor?,
		context: EffectContext
	): EffectModificationResult {
		if (descriptor != null) {
			val processor = processorFactory.createProcessor(descriptor.type)
			if (processor != null) {
				return processor.calculate(currentScore, descriptor, nextEffect, context)
			}
		}

		// Fallback: use effect label length if no descriptor/processor available
		return EffectModificationResult(
			scoreDelta = effect.label.length,
			glyphAmount = 0
		)
	}
}
