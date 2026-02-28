package com.cypherose.features.game.ui.animation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.common.components.ui.OutlinedText
import com.cypherose.features.game.presentation.models.animation.GlyphAnimationPayload
import com.cypherose.ui.theme.common

@Composable
internal fun GlyphDropAnimation(
	glyphAnimation: GlyphAnimationPayload,
	alpha: Float,
	glyphHorizontalOffset: Float,
	runeHorizontalOffset: Float,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier.alpha(alpha),
		contentAlignment = Alignment.Center
	) {
		if (glyphAnimation.glyphAmount > 0 && glyphAnimation.runeAmount > 0) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				CurrencyDropAnimationRow(
					amount = glyphAnimation.glyphAmount,
					icon = ImageState.Basic.GlyphIcon,
					modifier = Modifier.offset(x = glyphHorizontalOffset.dp)
				)
				CurrencyDropAnimationRow(
					amount = glyphAnimation.runeAmount,
					icon = ImageState.Basic.RuneIcon,
					modifier = Modifier.offset(x = runeHorizontalOffset.dp)
				)
			}
		} else {
			if (glyphAnimation.glyphAmount > 0) {
				CurrencyDropAnimationRow(
					amount = glyphAnimation.glyphAmount,
					icon = ImageState.Basic.GlyphIcon,
					modifier = Modifier.offset(x = glyphHorizontalOffset.dp)
				)
			}
			if (glyphAnimation.runeAmount > 0) {
				CurrencyDropAnimationRow(
					amount = glyphAnimation.runeAmount,
					icon = ImageState.Basic.RuneIcon,
					modifier = Modifier.offset(x = runeHorizontalOffset.dp)
				)
			}
		}
	}
}

@Composable
private fun CurrencyDropAnimationRow(
	amount: Int,
	icon: ImageState,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		OutlinedText(
			text = "$amount",
			textAlign = TextAlign.Center,
			textStyle = common.copy(fontSize = common.fontSize * 0.66f),
			modifier = Modifier,
			outlineWidth = 7,
			useGlow = false
		)
		Spacer(modifier = Modifier.width(8.dp))
		ImageRouter(
			state = icon,
			modifier = Modifier.size(30.dp)
		)
	}
}
