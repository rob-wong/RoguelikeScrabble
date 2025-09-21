package com.example.gymapprefactor.app.util

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.runtime.Composable

@Composable
fun Modifier.fitAspect(
	painter: Painter,
): Modifier {
	return this.aspectByParent(painter)
}

@Composable
private fun Modifier.aspectByParent(painter: Painter): Modifier {
	val intrinsicSize = painter.intrinsicSize
	val imageAspect = intrinsicSize.width / intrinsicSize.height

	return this
		.aspectRatio(imageAspect)
}
