package com.example.gymapprefactor.features.game.presentation.models

data class EffectAnimationPayload(
	val effectId: String,
	val effectLabel: String,
	val scoreDelta: Int,
	val orderIndex: Int,
	val cumulativeScore: Int,
	val multiplier: Double? = null // For multiplication effects, the multiplier to display (e.g., 2.0 for "x 2")
)
