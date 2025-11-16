package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import com.example.gymapprefactor.business.models.Effect
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.ui.theme.common
import com.example.gymapprefactor.ui.theme.rare
import com.example.gymapprefactor.ui.theme.uncommon

@Composable
fun EffectSelectionOption(
	effect: Effect,
	effectDescriptors: Map<String, EffectDescriptor>,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val descriptor = effect.descriptor ?: effectDescriptors[effect.label]
	val textStyle = getEffectTextStyle(descriptor)
	
	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp)
			.clip(RoundedCornerShape(8.dp))
			.background(stonePatternBrush())
			.clickable(onClick = onClick)
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		OutlinedText(
			text = effect.label,
			textAlign = TextAlign.Center,
			textStyle = textStyle,
			outlineWidth = 5,
			useGlow = false
		)
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

