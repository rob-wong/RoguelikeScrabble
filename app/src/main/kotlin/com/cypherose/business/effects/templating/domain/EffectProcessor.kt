package com.cypherose.business.effects.templating.domain

import com.cypherose.business.models.Effect

interface EffectProcessor {
	fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?,
		context: EffectContext?
	): EffectModificationResult
}
