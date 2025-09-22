package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.ui.theme.Typography

@Composable
fun ResourceBarRouter(
	state: ResourceBarState,
	modifier: Modifier = Modifier
) {
	when(state) {
		is ResourceBarState.Content -> ResourceBarLayout(state, modifier)
		is ResourceBarState.None -> Unit
	}
}

@Composable
private fun ResourceBarLayout(
	state: ResourceBarState.Content,
	modifier: Modifier = Modifier,
) {
	Box(modifier = modifier.widthIn(
		max = resourceBarMaxWidthRouter()
	)) {
		ResourceBarContent(state, Modifier)
	}
}

@Composable
private fun ResourceBarContent(
	state: ResourceBarState.Content,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier.wrapContentSize(),
		contentAlignment = Alignment.Center
	) {
		ImageRouter(
			state = ImageState.ResourceBarBackground,
			contentScale = ContentScale.Fit
		)

		Column(
			Modifier.height(IntrinsicSize.Max),
			verticalArrangement = Arrangement.Top,
		) {
			Spacer(Modifier.padding(top = 10.dp))
			Row(Modifier) {
				ResourceRouter(state.runeState, Modifier)
				Spacer(Modifier.padding(start = 20.dp))
				ResourceRouter(state.glyphState, Modifier)
			}
		}
	}
}

@Composable
private fun ResourceRouter(
	state: ResourceBarState.ResourceState,
	modifier: Modifier = Modifier
) {
	if (!state.isDisplayed) { return }
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
	) {
		ImageRouter(
			state.icon,
			modifier = Modifier.size(55.dp)
		)
		Text(
			modifier = Modifier,
			maxLines = 1,
			text = state.amount,
			style = Typography.bodyMedium
		)
	}
}
@Composable
private fun resourceBarMaxWidthRouter(): Dp {
	return when (DeviceUtil.isLandscape) {
		true -> DeviceUtil.getColumnWidthDp(5)
		false -> DeviceUtil.getColumnWidthDp(6)
	}
}

@Composable
@Preview
private fun ResourceBarPreview() {
	ResourceBarLayout(
		ResourceBarState.Content(
			runeState = ResourceBarState.ResourceState(
				isDisplayed = true,
				amount = "40",
				icon = ImageState.RuneIcon,
			),
			glyphState = ResourceBarState.ResourceState(
				isDisplayed = true,
				amount = "0",
				icon = ImageState.GlyphIcon,
			),
		)
	)
}
