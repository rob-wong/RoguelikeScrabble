package com.cypherose.common.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.presentation.ImageState

@Composable
fun ComingSoonOverlay(
	modifier: Modifier = Modifier,
	backgroundSize: Dp = 500.dp
) {
	val density = LocalDensity.current
	val backgroundSizePx = with(density) { backgroundSize.toPx() }
	val centerX = backgroundSizePx / 2f
	val centerY = backgroundSizePx / 2f
	
	Box(
		modifier = modifier
	) {
		// Circular darkening background with feathered edges - stronger at center, weaker at edges
		Box(
			modifier = Modifier
				.size(backgroundSize)
				.background(
					brush = Brush.radialGradient(
						colors = listOf(
							Color.Black.copy(alpha = 0.8f),  // Strongest at center
							Color.Black.copy(alpha = 0.6f),
							Color.Black.copy(alpha = 0.4f),
							Color.Black.copy(alpha = 0.15f),
							Color.Transparent  // Fully transparent at edges
						),
						center = Offset(centerX, centerY),
						radius = backgroundSizePx / 2f
					),
					shape = CircleShape
				)
		)
		ImageRouter(
			state = ImageState.ComingSoon,
			modifier = Modifier.align(Alignment.Center)
		)
	}
}
