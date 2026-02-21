package com.cypherose.common.components.buttons.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.cypherose.app.util.DevicePreviews
import com.cypherose.app.util.DeviceUtil
import com.cypherose.common.components.buttons.presentation.ImageButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.ui.ImageRouter

@Composable
fun ImageButtonRouter(
	state: ImageButtonState,
	modifier: Modifier = Modifier
) {
	when (state) {
		is ImageButtonState.Content -> ImageButtonContent(state, modifier)
		is ImageButtonState.None -> Unit
	}
}

@Composable
private fun ImageButtonContent(
	state: ImageButtonState.Content,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.widthIn(max = buttonWidthRouter())
			.wrapContentSize()
			.clickable(onClick = state.onClick),
		contentAlignment = Alignment.Center
	) {
		ImageRouter(
			state = state.background,
			contentScale = ContentScale.Fit,
			modifier = Modifier.fillMaxWidth()
		)
		ImageRouter(
			state = state.foreground,
			modifier = Modifier.fillMaxWidth()
		)
	}
}

@Composable
private fun buttonWidthRouter(): Dp {
	return when (DeviceUtil.isLandscape) {
		true -> DeviceUtil.getColumnWidthDp(5)
		false -> DeviceUtil.getColumnWidthDp(4)
	}
}

@DevicePreviews
@Composable
fun ImageButtonPreview() {
	ImageButtonContent(state = ImageButtonState.Content(
		onClick = { },
		background = ImageState.RectangularButtonBackground,
		foreground = ImageState.PlayTextIcon
	))
}
