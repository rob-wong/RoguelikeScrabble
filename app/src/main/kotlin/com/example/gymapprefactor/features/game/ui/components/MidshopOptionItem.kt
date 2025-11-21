package com.example.gymapprefactor.features.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.ui.theme.common

private fun Modifier.midshopClickable(
	isEnabled: Boolean,
	onClick: () -> Unit
): Modifier {
	return this.then(
		if (isEnabled) {
			Modifier.clickable(onClick = onClick)
		} else {
			Modifier
		}
	)
}

@Composable
fun MidshopOptionItem(
	option: MidshopOption,
	isSelected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val backgroundColor = getOptionColor(option.cost)
	val borderColor = if (isSelected) Color.White else Color.Transparent
	val borderWidth = if (isSelected) 4.dp else 0.dp
	
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.padding(horizontal = 4.dp, vertical = 4.dp)
			.background(
				color = backgroundColor,
				shape = RoundedCornerShape(12.dp)
			)
			.border(
				width = borderWidth,
				color = borderColor,
				shape = RoundedCornerShape(12.dp)
			)
			.midshopClickable(
				isEnabled = option.isEnabled,
				onClick = onClick
			)
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		OutlinedText(
			text = "${option.cost}",
			textAlign = TextAlign.Center,
			textStyle = common,
			outlineWidth = 5,
			useGlow = false
		)
		
		// Grey overlay when disabled
		if (!option.isEnabled) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(
						color = Color.Black.copy(alpha = 0.5f),
						shape = RoundedCornerShape(12.dp)
					)
			)
		}
	}
}

private fun getOptionColor(cost: Int): Color {
	return when (cost) {
		0 -> Color(0xFF808080) // Grey
		1 -> Color(0xFF4CAF50) // Green
		2 -> Color(0xFF2196F3) // Blue
		3 -> Color(0xFF9C27B0) // Purple
		4 -> Color(0xFFFF9800) // Orange
		else -> Color(0xFF808080) // Default grey
	}
}
