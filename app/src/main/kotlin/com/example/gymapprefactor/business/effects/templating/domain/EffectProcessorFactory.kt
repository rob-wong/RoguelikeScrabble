package com.example.gymapprefactor.business.effects.templating.domain

import com.example.gymapprefactor.business.effects.templating.domain.processors.ComboProcessor
import com.example.gymapprefactor.business.effects.templating.domain.processors.FixedAdditionProcessor
import com.example.gymapprefactor.business.effects.templating.domain.processors.MonetaryProcessor
import com.example.gymapprefactor.business.effects.templating.domain.processors.MultiplicationProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffectProcessorFactory @Inject constructor(
	fixedAdditionProcessor: FixedAdditionProcessor,
	multiplicationProcessor: MultiplicationProcessor,
	comboProcessor: ComboProcessor,
	monetaryProcessor: MonetaryProcessor
) {
	private val processors: Map<String, EffectProcessor> = mapOf(
		"fixed_addition" to fixedAdditionProcessor,
		"multiplication" to multiplicationProcessor,
		"combo" to comboProcessor,
		"monetary" to monetaryProcessor
	)

	fun createProcessor(type: String): EffectProcessor? {
		return processors[type]
	}

	fun isTypeSupported(type: String): Boolean {
		return processors.containsKey(type)
	}
}
