package com.cypherose.common.components.ui

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
import com.cypherose.app.util.DeviceUtil
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.ui.theme.Typography

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
				ResourceStateRouter(state.runeState, Modifier)
				Spacer(Modifier.padding(start = 20.dp))
				ResourceStateRouter(state.glyphState, Modifier)
			}
		}
	}
}

@Composable
private fun ResourceStateRouter(
	state: ResourceState,
	modifier: Modifier = Modifier
) {
	when(state) {
		is ResourceState.Content -> ResourceStateContent(state, modifier)
		is ResourceState.None -> Unit
	}
}

@Composable
private fun ResourceStateContent(
	state: ResourceState.Content,
	modifier: Modifier = Modifier
) {
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
			runeState = ResourceState.Content(
				amount = "40",
				icon = ImageState.RuneIcon,
			),
			glyphState = ResourceState.Content(
				amount = "0",
				icon = ImageState.GlyphIcon,
			),
		)
	)
}
