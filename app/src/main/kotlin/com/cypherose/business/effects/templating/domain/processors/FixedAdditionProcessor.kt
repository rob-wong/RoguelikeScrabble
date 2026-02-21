package com.cypherose.business.effects.templating.domain.processors

import com.cypherose.business.effects.templating.domain.EffectContext
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.effects.templating.domain.EffectModificationResult
import com.cypherose.business.effects.templating.domain.EffectProcessor
import com.cypherose.business.effects.templating.domain.FixedAdditionConfig
import com.cypherose.business.models.Effect
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject

class FixedAdditionProcessor @Inject constructor(
	private val json: Json
) : EffectProcessor {

	override fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?,
		context: EffectContext?
	): EffectModificationResult {
		val config = json.decodeFromJsonElement(
			serializer<FixedAdditionConfig>(),
			descriptor.config
		)
		return EffectModificationResult(
			scoreDelta = config.value,
			glyphAmount = 0
		)
	}
}
