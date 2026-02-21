package com.cypherose.features.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.cypherose.app.util.DeviceUtil
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import com.cypherose.business.effects.templating.domain.isChanceEffect
import com.cypherose.business.models.Effect
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.common.components.ui.OutlinedText
import com.cypherose.features.game.ui.animation.EffectAnimationState
import com.cypherose.ui.theme.chance
import com.cypherose.ui.theme.chanceMultiplierNegativeColor
import com.cypherose.ui.theme.chanceMultiplierPositiveColor
import com.cypherose.ui.theme.chanceOscillatingColors
import com.cypherose.ui.theme.common
import com.cypherose.ui.theme.legendary
import com.cypherose.ui.theme.rare
import com.cypherose.ui.theme.uncommon
import kotlin.math.roundToInt

@Suppress("LongMethod", "TooManyFunctions")
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
			key = { effect ->
				val key = "active_${effect.id}"
				key
			}
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
			key = { effect ->
				val key = "round_${effect.id}"
				key
			}
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
	
	// Scroll to make the effect the first visible item
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
	val isChanceMultiplier = effectState?.effectChanceMultiplierMap?.get(effect.id) ?: false
	val glyphAmount = effectState?.effectGlyphAmountMap?.get(effect.id)
	
	val descriptor = effect.descriptor ?: effectDescriptors[effect.label]
	val isChanceEffectType = try {
		descriptor?.isChanceEffect() ?: false
	} catch (_: Exception) {
		false
	}
	val textStyle = if (isChanceEffectType) {
		chance
	} else {
		getEffectTextStyle(descriptor)
	}
	Row(
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(
			modifier = createEffectRowModifier(shake, hasStoneBackground),
		) {
			if (isChanceEffectType) {
				// For chance multipliers, always oscillate colors on the effect label
				OscillatingColorText(
					text = effect.label,
					baseTextStyle = textStyle.copy(
						fontSize = textStyle.fontSize * 0.66
					),
					colors = chanceOscillatingColors,
					outlineWidth = 5,
					useGlow = false
				)
			} else {
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
		}
		renderEffectModifier(multiplier, isChanceMultiplier, scoreDelta, glyphAmount, scoreAlpha)
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
	isChanceMultiplier: Boolean,
	scoreDelta: Int?,
	glyphAmount: Int?,
	scoreAlpha: Float
) {
	val modifier = Modifier.graphicsLayer(alpha = scoreAlpha).padding(start = 8.dp)
	val textStyle = common.copy(fontSize = common.fontSize * 0.66)
	
	when {
		glyphAmount != null && scoreAlpha > 0f -> {
			Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
				OutlinedText(
					text = "+$glyphAmount",
					textAlign = TextAlign.Center,
					textStyle = textStyle,
					outlineWidth = 4,
					useGlow = false
				)
				ImageRouter(
					state = ImageState.GlyphIcon,
					modifier = Modifier.padding(start = 4.dp).size(20.dp)
				)
			}
		}
		multiplier != null && scoreAlpha > 0f -> {
			if (isChanceMultiplier) {
				ChanceMultiplierText(
					multiplier = multiplier,
					baseTextStyle = chance.copy(fontSize = chance.fontSize * 0.66),
					modifier = modifier
				)
			} else {
				OutlinedText(
					text = "x ${multiplier.toInt()}",
					textAlign = TextAlign.Center,
					textStyle = textStyle,
					outlineWidth = 4,
					useGlow = false,
					modifier = modifier
				)
			}
		}
		scoreDelta != null && scoreAlpha > 0f -> {
			OutlinedText(
				text = "+$scoreDelta",
				textAlign = TextAlign.Center,
				textStyle = textStyle,
				outlineWidth = 4,
				useGlow = false,
				modifier = modifier
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
private fun ChanceMultiplierText(
	multiplier: Double,
	baseTextStyle: TextStyle,
	modifier: Modifier = Modifier
) {
	// Format multiplier to 2 decimal places - this is the presentation state requirement
	val formattedText = "x ${String.format(java.util.Locale.US, "%.2f", multiplier)}"
	
	// Green if above x1, red if below
	val multiplierColor = if (multiplier >= 1.0) {
		chanceMultiplierPositiveColor
	} else {
		chanceMultiplierNegativeColor
	}
	
	OutlinedText(
		text = formattedText,
		textAlign = TextAlign.Center,
		textStyle = baseTextStyle.copy(color = multiplierColor),
		outlineWidth = 4,
		useGlow = false,
		modifier = modifier
	)
}

@Composable
private fun OscillatingColorText(
	text: String,
	baseTextStyle: TextStyle,
	colors: List<Color>,
	modifier: Modifier = Modifier,
	outlineWidth: Int = 4,
	useGlow: Boolean = false,
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		text.forEachIndexed { index, char ->
			val color = colors[index % colors.size]
			val charTextStyle = baseTextStyle.copy(color = color)
			
			OutlinedText(
				text = char.toString(),
				textAlign = TextAlign.Center,
				textStyle = charTextStyle,
				outlineWidth = outlineWidth,
				useGlow = useGlow
			)
		}
	}
}

@Composable
private fun getEffectTextStyle(descriptor: EffectDescriptor?): TextStyle {
	return when {
		descriptor == null -> common
		descriptor.type == "fixed_addition" -> uncommon
		descriptor.type == "multiplication" -> rare
		descriptor.type == "monetary" -> legendary
		else -> common
	}
}
