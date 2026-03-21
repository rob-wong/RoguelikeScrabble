package com.cypherose.common.components.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.cypherose.R

private object RoseBackdropDefaults {
	val LEFT_PADDING = 25.dp
	val RIGHT_PADDING = 5.dp
	val VERTICAL_PADDING = 4.dp
}

@Composable
fun RoseBackdropLabel(
	text: String,
	textStyle: TextStyle,
	showBackdrop: Boolean,
	modifier: Modifier = Modifier,
	leftPadding: Dp = RoseBackdropDefaults.LEFT_PADDING,
	rightPadding: Dp = RoseBackdropDefaults.RIGHT_PADDING,
	verticalPadding: Dp = RoseBackdropDefaults.VERTICAL_PADDING,
	textAlign: TextAlign = TextAlign.Center,
	outlineWidth: Int = 5,
	useGlow: Boolean = false,
	oscillatingColors: List<Color>? = null,
) {
	val textSize = rememberMeasuredTextSize(text = text, textStyle = textStyle)
	val density = LocalDensity.current
	val measuredWidthDp = with(density) { textSize.width.toDp() + leftPadding + rightPadding }
	val measuredHeightDp = with(density) { textSize.height.toDp() + (verticalPadding * 2) }

	RoseBackdropContainer(
		modifier = modifier
			.width(measuredWidthDp)
			.height(measuredHeightDp)
			.then(roseBackdropModifier(showBackdrop)),
	) {
		RoseLabelContent(
			text = text,
			textStyle = textStyle,
			textAlign = textAlign,
			outlineWidth = outlineWidth,
			useGlow = useGlow,
			leftPadding = leftPadding,
			rightPadding = rightPadding,
			verticalPadding = verticalPadding,
			oscillatingColors = oscillatingColors
		)
	}
}

@Composable
private fun rememberMeasuredTextSize(text: String, textStyle: TextStyle): IntSize {
	val textMeasurer = rememberTextMeasurer()
	return remember(text, textStyle) {
		textMeasurer.measure(
			text = AnnotatedString(text),
			style = textStyle
		).size
	}
}

@Composable
private fun roseBackdropModifier(showBackdrop: Boolean): Modifier {
	if (!showBackdrop) return Modifier.clipToBounds()
	return Modifier
		.clipToBounds()
		.paint(
			painter = painterResource(R.drawable.image_rose_effect_backdrop),
			alignment = Alignment.CenterStart,
			contentScale = ContentScale.FillHeight
		)
}

@Composable
private fun RoseBackdropContainer(
	modifier: Modifier,
	content: @Composable () -> Unit
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.CenterStart
	) {
		content()
	}
}

@Composable
private fun RoseLabelContent(
	text: String,
	textStyle: TextStyle,
	textAlign: TextAlign,
	outlineWidth: Int,
	useGlow: Boolean,
	leftPadding: Dp,
	rightPadding: Dp,
	verticalPadding: Dp,
	oscillatingColors: List<Color>?,
) {
	Box(
		modifier = Modifier.padding(
			start = leftPadding,
			end = rightPadding,
			top = verticalPadding,
			bottom = verticalPadding
		)
	) {
		if (oscillatingColors == null) {
			OutlinedText(
				text = text,
				textAlign = textAlign,
				textStyle = textStyle,
				outlineWidth = outlineWidth,
				useGlow = useGlow
			)
		} else {
			OscillatingOutlinedText(
				text = text,
				textStyle = textStyle,
				textAlign = textAlign,
				outlineWidth = outlineWidth,
				useGlow = useGlow,
				colors = oscillatingColors
			)
		}
	}
}

@Composable
private fun OscillatingOutlinedText(
	text: String,
	textStyle: TextStyle,
	textAlign: TextAlign,
	outlineWidth: Int,
	useGlow: Boolean,
	colors: List<Color>,
) {
	Row(verticalAlignment = Alignment.CenterVertically) {
		text.forEachIndexed { index, char ->
			OutlinedText(
				text = char.toString(),
				textAlign = textAlign,
				textStyle = textStyle.copy(color = colors[index % colors.size]),
				outlineWidth = outlineWidth,
				useGlow = useGlow
			)
		}
	}
}
