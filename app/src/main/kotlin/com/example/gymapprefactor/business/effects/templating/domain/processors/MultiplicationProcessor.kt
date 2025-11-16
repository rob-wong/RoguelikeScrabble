package com.example.gymapprefactor.business.effects.templating.domain.processors

import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
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
		nextEffect: Effect?
	): Int {
		val config = json.decodeFromJsonElement(
			serializer<MultiplicationConfig>(),
			descriptor.config
		)
		// Return the delta (change in score), not the new total score
		val newScore = (currentScore * config.multiplier).toInt()
		return newScore - currentScore
	}
}
