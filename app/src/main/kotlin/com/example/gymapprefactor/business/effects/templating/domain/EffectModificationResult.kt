package com.example.gymapprefactor.business.effects.templating.domain

data class EffectModificationResult(
	val scoreDelta: Int = 0,
	val glyphAmount: Int = 0,
	val multiplier: Double? = null, // For multiplication effects, the multiplier used
	val isChanceMultiplier: Boolean = false // True if this is a chance-type multiplication effect
)
