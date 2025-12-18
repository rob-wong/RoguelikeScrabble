package com.example.gymapprefactor.features.game.presentation.models.animation

sealed class EffectAnimationPayload {
	abstract val effectId: String
	abstract val effectLabel: String
	abstract val orderIndex: Int

	data class Score(
		override val effectId: String,
		override val effectLabel: String,
		val scoreDelta: Int,
		override val orderIndex: Int,
		val cumulativeScore: Int,
		val multiplier: Double? = null // For multiplication effects, the multiplier to display (e.g., 2.0 for "x 2")
	) : EffectAnimationPayload()

	data class Glyph(
		override val effectId: String,
		override val effectLabel: String,
		val glyphAmount: Int,
		override val orderIndex: Int
	) : EffectAnimationPayload()
}
