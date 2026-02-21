package com.cypherose.features.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.features.game.presentation.models.midshop.MidshopOptionState

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
	optionState: MidshopOptionState,
	isSelected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val borderColor = if (isSelected) Color.White else Color.Transparent
	val borderWidth = if (isSelected) 4.dp else 0.dp
	
	Box(
		modifier = modifier
			.padding(horizontal = 4.dp, vertical = 4.dp)
			.border(
				width = borderWidth,
				color = borderColor,
				shape = RoundedCornerShape(12.dp)
			)
			.midshopClickable(
				isEnabled = optionState.option.isEnabled,
				onClick = onClick
			)
			.wrapContentSize(),
		contentAlignment = Alignment.Center
	) {
		ImageRouter(
			state = optionState.imageState,
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.Fit
		)
		
		// Grey overlay when disabled
		if (!optionState.option.isEnabled) {
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
