package com.cypherose.business.effects.templating.domain

import kotlin.random.Random

fun EffectContext?.calculateChanceMultiplier(
	minMultiplier: Double,
	maxMultiplier: Double
): Double {
	if (this == null) {
		// Fallback to midpoint if no context available
		return (minMultiplier + maxMultiplier) / 2.0
	}
	
	val combinedSeed = combineSeedForEffect()
	val random = Random(combinedSeed)
	val range = maxMultiplier - minMultiplier
	return minMultiplier + (range * random.nextDouble())
}
