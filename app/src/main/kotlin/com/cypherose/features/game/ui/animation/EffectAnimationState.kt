package com.cypherose.features.game.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class EffectAnimationState(
	val effectShakeMap: MutableMap<String, Animatable<Float, AnimationVector1D>>,
	val effectScoreAlphaMap: MutableMap<String, Animatable<Float, AnimationVector1D>>,
	val effectScoreValueMap: MutableMap<String, Int>,
	val effectMultiplierMap: MutableMap<String, Double>,
	val effectChanceMultiplierMap: MutableMap<String, Boolean>,
	val effectGlyphAmountMap: MutableMap<String, Int>,
	val totalScoreState: MutableState<Int?>,
	val totalScoreShake: Animatable<Float, AnimationVector1D>,
) {
	var totalScore: Int?
		get() = totalScoreState.value
		set(value) { totalScoreState.value = value }

	suspend fun clear() {
		effectShakeMap.clear()
		effectScoreAlphaMap.clear()
		effectScoreValueMap.clear()
		effectMultiplierMap.clear()
		effectChanceMultiplierMap.clear()
		effectGlyphAmountMap.clear()
		totalScoreState.value = null
		totalScoreShake.snapTo(0f)
	}
}

@Composable
internal fun rememberEffectAnimationState(): EffectAnimationState {
	val effectShakeMap = remember { mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>() }
	val effectScoreAlphaMap = remember { mutableStateMapOf<String, Animatable<Float, AnimationVector1D>>() }
	val effectScoreValueMap = remember { mutableStateMapOf<String, Int>() }
	val effectMultiplierMap = remember { mutableStateMapOf<String, Double>() }
	val effectChanceMultiplierMap = remember { mutableStateMapOf<String, Boolean>() }
	val effectGlyphAmountMap = remember { mutableStateMapOf<String, Int>() }
	val totalScoreState = remember { mutableStateOf<Int?>(null) }
	val totalScoreShake = remember { Animatable(0f) }

	return remember {
		EffectAnimationState(
			effectShakeMap = effectShakeMap,
			effectScoreAlphaMap = effectScoreAlphaMap,
			effectScoreValueMap = effectScoreValueMap,
			effectMultiplierMap = effectMultiplierMap,
			effectChanceMultiplierMap = effectChanceMultiplierMap,
			effectGlyphAmountMap = effectGlyphAmountMap,
			totalScoreState = totalScoreState,
			totalScoreShake = totalScoreShake
		)
	}
}
