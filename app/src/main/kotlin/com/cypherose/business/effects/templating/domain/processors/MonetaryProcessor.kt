package com.cypherose.business.effects.templating.domain.processors

import com.cypherose.business.effects.templating.domain.EffectContext
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.effects.templating.domain.EffectModificationResult
import com.cypherose.business.effects.templating.domain.EffectProcessor
import com.cypherose.business.effects.templating.domain.MonetaryConfig
import com.cypherose.business.models.Effect
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject

class MonetaryProcessor @Inject constructor(
	private val json: Json
) : EffectProcessor {

	override fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?,
		context: EffectContext?
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
