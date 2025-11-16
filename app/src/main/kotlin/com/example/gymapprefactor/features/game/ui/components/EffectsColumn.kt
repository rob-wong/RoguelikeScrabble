package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
	effects: List<Effect>,
	effectState: EffectAnimationState?,
	effectDescriptors: Map<String, EffectDescriptor>,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier = modifier
			.width(DeviceUtil.getColumnWidthDp(5))
			.height(200.dp)
	) {
		items(
			items = effects,
			key = { effect -> effect.id }
		) { effect ->
			val shake = effectState?.effectShakeMap?.get(effect.id)?.value ?: 0f
			val scoreAlpha = effectState?.effectScoreAlphaMap?.get(effect.id)?.value ?: 0f
			val scoreDelta = effectState?.effectScoreValueMap?.get(effect.id)
			val multiplier = effectState?.effectMultiplierMap?.get(effect.id)
			
			// Get effect descriptor to determine font color
			val descriptor = effect.descriptor ?: effectDescriptors[effect.label]
			val textStyle = getEffectTextStyle(descriptor)

			Row(
				modifier = Modifier
					.padding(vertical = 4.dp)
					.offset { IntOffset(shake.roundToInt(), 0) },
				verticalAlignment = Alignment.CenterVertically
			) {
				OutlinedText(
					text = effect.label,
					textAlign = TextAlign.Center,
					textStyle = textStyle,
					outlineWidth = 5,
					useGlow = false
				)
				// Show multiplier for multiplication effects, delta for addition effects
				if (multiplier != null && scoreAlpha > 0f) {
					OutlinedText(
						text = "x ${multiplier.toInt()}",
						textAlign = TextAlign.Center,
						textStyle = com.example.gymapprefactor.ui.theme.common.copy(
							fontSize = com.example.gymapprefactor.ui.theme.common.fontSize * 0.8f
						),
						outlineWidth = 4,
						useGlow = false,
						modifier = Modifier
							.graphicsLayer(alpha = scoreAlpha)
							.padding(start = 8.dp)
					)
				} else if (scoreDelta != null && scoreAlpha > 0f) {
					OutlinedText(
						text = "+$scoreDelta",
						textAlign = TextAlign.Center,
						textStyle = com.example.gymapprefactor.ui.theme.common.copy(
							fontSize = com.example.gymapprefactor.ui.theme.common.fontSize * 0.8f
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
	}
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
