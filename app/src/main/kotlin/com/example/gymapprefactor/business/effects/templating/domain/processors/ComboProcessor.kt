package com.example.gymapprefactor.business.effects.templating.domain.processors

import com.example.gymapprefactor.business.effects.templating.domain.ComboConfig
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessor
import com.example.gymapprefactor.business.effects.templating.domain.EffectProcessorFactory
import com.example.gymapprefactor.business.models.Effect
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Provider

class ComboProcessor @Inject constructor(
	private val json: Json,
	private val processorFactoryProvider: Provider<EffectProcessorFactory>
) : EffectProcessor {

	override fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?
	): Int {
		val config = json.decodeFromJsonElement(
			serializer<ComboConfig>(),
			descriptor.config
		)

		val shouldUseCombo = nextEffect?.let { effect ->
			config.condition.matches(effect)
		} ?: false

		val effectToApply = if (shouldUseCombo) {
			config.comboEffect
		} else {
			config.defaultEffect
		}

		val processorFactory = processorFactoryProvider.get()
		val processor = processorFactory.createProcessor(effectToApply.type)
			?: return 0

		// For combo effects, we need to recursively process
		// Pass nextEffect through so nested combos can also check conditions
		// This allows chained combo effects to work correctly
		return processor.calculate(currentScore, effectToApply, nextEffect)
	}
}
