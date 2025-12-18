package com.example.gymapprefactor.business.effects.templating.domain.processors

import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.effects.templating.domain.EffectModificationResult
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessor
import com.example.gymapprefactor.business.effects.templating.domain.MonetaryConfig
import com.example.gymapprefactor.business.models.Effect
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject

class MonetaryProcessor @Inject constructor(
	private val json: Json
) : EffectProcessor {

	override fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?
	): EffectModificationResult {
		val config = json.decodeFromJsonElement(
			serializer<MonetaryConfig>(),
			descriptor.config
		)
		// For now, only "glyph" type is supported
		// In the future, could support "rune" type as well
		val glyphAmount = if (config.type == "glyph") {
			config.value
		} else {
			0
		}
		return EffectModificationResult(
			scoreDelta = 0,
			glyphAmount = glyphAmount
		)
	}
}
