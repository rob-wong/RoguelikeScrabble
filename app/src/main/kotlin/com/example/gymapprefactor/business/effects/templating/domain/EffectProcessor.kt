package com.example.gymapprefactor.business.effects.templating.domain

import com.example.gymapprefactor.business.models.Effect

interface EffectProcessor {
	fun calculate(
		currentScore: Int,
		descriptor: EffectDescriptor,
		nextEffect: Effect?
	): Int
}
