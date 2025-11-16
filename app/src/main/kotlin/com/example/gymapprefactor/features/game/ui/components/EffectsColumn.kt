package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.features.game.ui.EffectAnimationState
import com.example.gymapprefactor.ui.theme.common
import com.example.gymapprefactor.ui.theme.rare
import com.example.gymapprefactor.ui.theme.uncommon
import kotlin.math.roundToInt

@SuppressWarnings("LongMethod")
@Composable
fun EffectsColumn(
	activeGameEffects: List<Effect>,
	currentRoundEffects: List<Effect>,
	effectState: EffectAnimationState?,
	effectDescriptors: Map<String, EffectDescriptor>,
	animatingEffectId: String?,
	modifier: Modifier = Modifier,
) {
	val listState = rememberLazyListState()
	
	// Scroll to animating effect when it starts
	LaunchedEffect(animatingEffectId) {
		if (animatingEffectId != null) {
			scrollToEffect(listState, animatingEffectId, activeGameEffects, currentRoundEffects)
		}
	}
	
	LazyColumn(
		state = listState,
		modifier = modifier
			.width(DeviceUtil.getColumnWidthDp(5))
			.height(200.dp)
	) {
		// Render activeGameEffects with stone pattern background
		items(
			items = activeGameEffects,
			key = { effect -> effect.id }
		) { effect ->
			EffectItemWithBackground(
				effect = effect,
				effectState = effectState,
				effectDescriptors = effectDescriptors,
				hasStoneBackground = true
			)
		}
		
		// Render currentRoundEffects without background
		items(
			items = currentRoundEffects,
			key = { effect -> effect.id }
		) { effect ->
			EffectItemWithBackground(
				effect = effect,
				effectState = effectState,
				effectDescriptors = effectDescriptors,
				hasStoneBackground = false
			)
		}
	}
}

private suspend fun scrollToEffect(
	listState: LazyListState,
	effectId: String,
	activeGameEffects: List<Effect>,
	currentRoundEffects: List<Effect>
) {
	// Find the index of the effect in the combined list
	val activeIndex = activeGameEffects.indexOfFirst { it.id == effectId }
	val currentRoundIndex = currentRoundEffects.indexOfFirst { it.id == effectId }
	
	val targetIndex = when {
		activeIndex >= 0 -> activeIndex
		currentRoundIndex >= 0 -> activeGameEffects.size + currentRoundIndex
		else -> return // Effect not found, don't scroll
	}
	
	// Scroll to make the effect the first visible item (scrollOffset = 0)
	// This will scroll until it reaches the top or can't scroll anymore
	listState.animateScrollToItem(
		index = targetIndex,
		scrollOffset = 0
	)
}

@Composable
private fun EffectItemWithBackground(
	effect: Effect,
	effectState: EffectAnimationState?,
	effectDescriptors: Map<String, EffectDescriptor>,
	hasStoneBackground: Boolean,
) {
	val shake = effectState?.effectShakeMap?.get(effect.id)?.value ?: 0f
	val scoreAlpha = effectState?.effectScoreAlphaMap?.get(effect.id)?.value ?: 0f
	val scoreDelta = effectState?.effectScoreValueMap?.get(effect.id)
	val multiplier = effectState?.effectMultiplierMap?.get(effect.id)
	
	val descriptor = effect.descriptor ?: effectDescriptors[effect.label]
	val textStyle = getEffectTextStyle(descriptor)
	Row(
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(
			modifier = createEffectRowModifier(shake, hasStoneBackground),
		) {
			OutlinedText(
				text = effect.label,
				textAlign = TextAlign.Center,
				textStyle = textStyle.copy(
					fontSize = textStyle.fontSize * 0.66
				),
				outlineWidth = 5,
				useGlow = false
			)
		}
		renderEffectModifier(multiplier, scoreDelta, scoreAlpha)
	}
}

@Composable
private fun createEffectRowModifier(shake: Float, hasStoneBackground: Boolean): Modifier {
	return Modifier
		.padding(vertical = 4.dp)
		.offset { IntOffset(shake.roundToInt(), 0) }
		.then(
			if (hasStoneBackground) {
				Modifier
					.padding(horizontal = 4.dp)
					.background(
						brush = stonePatternBrush(),
						shape = RoundedCornerShape(4.dp)
					)
					.padding(horizontal = 8.dp, vertical = 4.dp)
			} else {
				Modifier
			}
		)
}

@Composable
private fun renderEffectModifier(
	multiplier: Double?,
	scoreDelta: Int?,
	scoreAlpha: Float
) {
	when {
		multiplier != null && scoreAlpha > 0f -> {
			OutlinedText(
				text = "x ${multiplier.toInt()}",
				textAlign = TextAlign.Center,
				textStyle = common.copy(
					fontSize = common.fontSize * 0.66
				),
				outlineWidth = 4,
				useGlow = false,
				modifier = Modifier
					.graphicsLayer(alpha = scoreAlpha)
					.padding(start = 8.dp)
			)
		}
		scoreDelta != null && scoreAlpha > 0f -> {
			OutlinedText(
				text = "+$scoreDelta",
				textAlign = TextAlign.Center,
				textStyle = common.copy(
					fontSize = common.fontSize * 0.66
				),
				outlineWidth = 4,
				useGlow = false,
				modifier = Modifier
					.graphicsLayer(alpha = scoreAlpha)
					.padding(start = 8.dp)
			)
		}
	}
}

@Composable
private fun stonePatternBrush(): Brush {
	// Create a grey stone-pattern effect using a gradient
	val baseGrey = Color(0xFF808080)
	val lightGrey = Color(0xFFA0A0A0)
	val darkGrey = Color(0xFF606060)
	
	return Brush.verticalGradient(
		colors = listOf(
			lightGrey,
			baseGrey,
			darkGrey,
			baseGrey,
			lightGrey
		)
	)
}

@Composable
private fun getEffectTextStyle(descriptor: EffectDescriptor?): TextStyle {
	return when {
		descriptor == null -> common
		descriptor.type == "fixed_addition" -> uncommon
		descriptor.type == "multiplication" -> rare
		else -> common
	}
}
