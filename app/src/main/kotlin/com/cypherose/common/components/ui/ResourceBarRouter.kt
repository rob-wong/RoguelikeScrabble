package com.cypherose.common.components.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cypherose.app.util.DevicePreviews
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

// Height multiplier for 9-patch resource bar - adjust this value to change height
private const val RESOURCE_BAR_HEIGHT_MULTIPLIER = 2f // Increase to make taller, decrease to make shorter

@Composable
private fun ResourceBarContent(
	state: ResourceBarState.Content,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		ImageRouter(
			state = ImageState.NinePatch.ResourceBarBackground,
			modifier = Modifier.fillMaxWidth(),
			contentScale = ContentScale.FillWidth,
			heightMultiplier = RESOURCE_BAR_HEIGHT_MULTIPLIER
		)

		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(20.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				ResourceStateRouter(state.runeState, Modifier)
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
@DevicePreviews
private fun ResourceBarPreview() {
	ResourceBarLayout(
		ResourceBarState.Content(
			runeState = ResourceState.Content(
				amount = "40",
				icon = ImageState.Basic.RuneIcon,
			),
			glyphState = ResourceState.Content(
				amount = "0",
				icon = ImageState.Basic.GlyphIcon,
			),
		)
	)
}
