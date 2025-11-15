package com.example.gymapprefactor.features.game.presentation.models

data class EffectAnimationPayload(
	val effectId: String,
	val effectLabel: String,
	val scoreDelta: Int,
	val orderIndex: Int,
	val cumulativeScore: Int
)
