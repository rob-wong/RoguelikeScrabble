package com.cypherose.common.components.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import kotlin.random.Random

@Composable
fun OutlinedText(
	text: String,
	textAlign: TextAlign,
	textStyle: TextStyle,
	modifier: Modifier = Modifier,
	outlineWidth: Int = 20,
	useGlow: Boolean = false,
) {
	val strokeStyle = textStyle.copy(
		color = Color.Black,
		drawStyle = Stroke(width = outlineWidth.toFloat())
	)

	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		GlowText(
			text = text,
			textStyle = textStyle,
			enabled = useGlow
		)

		Text(
			text = text,
			textAlign = textAlign,
			maxLines = 1,
			style = strokeStyle
		)
		Text(
			text = text,
			textAlign = textAlign,
			maxLines = 1,
			style = textStyle
		)
	}
}

@Composable
private fun GlowText(
	text: String,
	textStyle: TextStyle,
	enabled: Boolean,
) {
	if (enabled) {
		val glowColour = textStyle.color

		Particles(
			sparkleColor = glowColour
		)

		// Outer soft glow
		Text(
			text = text,
			style = textStyle.copy(
				shadow = Shadow(color = glowColour, blurRadius = 180f)
			)
		)

		// Inner tight glow
		Text(
			text = text,
			style = textStyle.copy(
				shadow = Shadow(color = glowColour, blurRadius = 40f)
			)
		)
	}
}

@Composable
private fun Particles(
	modifier: Modifier = Modifier,
	sparkleColor: Color = Color(0xFFFFF8E7),
	sparkleCount: Int = 5
) {
	val transition = rememberInfiniteTransition(label = "")
	val offsets = remember { List(sparkleCount) { Random.nextFloat() to Random.nextFloat() } }

	val alphaAnim = transition.animateFloat(
		initialValue = 0.2f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			animation = tween(1500, easing = LinearEasing),
			repeatMode = RepeatMode.Reverse
		),
		label = ""
	)

	Canvas(modifier = modifier) {
		offsets.forEach { (x, y) ->
			drawCircle(
				color = sparkleColor.copy(alpha = alphaAnim.value * Random.nextFloat()),
				radius = Random.nextFloat() * 4f + 1f,
				center = Offset(size.width * x, size.height * y)
			)
		}
	}
}

