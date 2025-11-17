package com.example.gymapprefactor.features.game.ui.animation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ImageRouter
import com.example.gymapprefactor.common.components.ui.OutlinedText
import com.example.gymapprefactor.features.game.presentation.models.animation.GlyphAnimationPayload
import com.example.gymapprefactor.ui.theme.common

@Composable
internal fun GlyphDropAnimation(
	glyphAnimation: GlyphAnimationPayload,
	alpha: Float,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier
			.alpha(alpha),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		OutlinedText(
			text = "${glyphAnimation.amount}",
			textAlign = TextAlign.Center,
			textStyle = common.copy(fontSize = common.fontSize * 0.66f),
			modifier = Modifier,
			outlineWidth = 7,
			useGlow = false
		)
		Spacer(modifier = Modifier.width(8.dp))
		ImageRouter(
			state = ImageState.GlyphIcon,
			modifier = Modifier.size(30.dp)
		)
	}
}
