package com.example.gymapprefactor.business.effects.templating.domain.processors

import com.example.gymapprefactor.business.effects.templating.domain.EffectContext
import com.example.gymapprefactor.business.effects.templating.domain.calculateChanceMultiplier
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.effects.templating.domain.EffectModificationResult
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessor
import com.example.gymapprefactor.business.effects.templating.domain.MultiplicationConfig
import com.example.gymapprefactor.business.models.Effect
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject

class MultiplicationProcessor @Inject constructor(
	private val json: Json
) : EffectProcessor {

	override fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?,
		context: EffectContext?
	): EffectModificationResult {
		val config = json.decodeFromJsonElement(
			serializer<MultiplicationConfig>(),
			descriptor.config
		)
		
		val isChance = config.type == "chance"
		val multiplier = if (isChance) {
			val minMultiplier = config.minMultiplier ?: 1.0
			val maxMultiplier = config.maxMultiplier ?: 2.0
			context?.calculateChanceMultiplier(minMultiplier, maxMultiplier)
				?: ((minMultiplier + maxMultiplier) / 2.0)
		} else {
			config.multiplier ?: 1.0
		}
		
		// Return the delta (change in score), not the new total score
		val newScore = (currentScore * multiplier).toInt()
		val scoreDelta = newScore - currentScore
		return EffectModificationResult(
			scoreDelta = scoreDelta,
			glyphAmount = 0,
			multiplier = multiplier,
			isChanceMultiplier = isChance
		)
	}
}
