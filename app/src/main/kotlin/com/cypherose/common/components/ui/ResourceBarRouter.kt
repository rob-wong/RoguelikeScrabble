@file:Suppress("TooManyFunctions")

package com.cypherose.common.components.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.graphics.drawable.NinePatchDrawable
import com.cypherose.R
import com.cypherose.app.util.DevicePreviews
import com.cypherose.app.util.DeviceUtil
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.ui.theme.Typography

private const val RESOURCE_BAR_HEIGHT_MULTIPLIER = 2f
private const val MIN_HORIZONTAL_LAYOUT_WIDTH_DP = 120f

private const val FIXED_EDGE_WIDTH_PERCENTAGE = 0.1f
private const val MAX_FIXED_EDGE_PERCENTAGE_OF_TOTAL = 0.15f
private const val MIN_FIXED_EDGE_WIDTH_PX = 50
private const val MAX_FIXED_EDGE_WIDTH_PX = 200

private const val HORIZONTAL_RESOURCE_SPACING_DP = 20
private const val VERTICAL_RESOURCE_SPACING_DP = 4

private const val HORIZONTAL_ICON_SIZE_DP = 45
private const val VERTICAL_ICON_SIZE_DP = 25

private data class LayoutDecision(
	val useHorizontalLayout: Boolean
)

private data class NinePatchInfo(
	val intrinsicWidthPx: Int,
	val originalPadding: Rect
)

private data class FixedEdgeWidths(
	val left: Int,
	val right: Int
)

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
	val context = LocalContext.current
	val ninePatchInfo = remember {
		getNinePatchInfo(context, R.drawable.resource_bar_v2)
	}
	
	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		ResourceBarBackground()
		ResourceContent(state, ninePatchInfo)
	}
}

@Composable
private fun ResourceBarBackground() {
	ImageRouter(
		state = ImageState.NinePatch.ResourceBarBackground,
		modifier = Modifier.fillMaxWidth(),
		contentScale = ContentScale.FillWidth,
		heightMultiplier = RESOURCE_BAR_HEIGHT_MULTIPLIER
	)
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun ResourceContent(
	state: ResourceBarState.Content,
	ninePatchInfo: NinePatchInfo?
) {
	val density = LocalDensity.current
	
	BoxWithConstraints(
		modifier = Modifier.fillMaxWidth()
	) {
		val layoutDecision = calculateLayoutDecision(ninePatchInfo, maxWidth, density)
		
		ResourceContentLayout(
			state = state,
			useHorizontalLayout = layoutDecision.useHorizontalLayout
		)
	}
}

private fun calculateLayoutDecision(
	ninePatchInfo: NinePatchInfo?,
	maxWidth: Dp,
	density: Density
): LayoutDecision {
	val totalWidthPx = with(density) { maxWidth.toPx() }
	val fixedEdgeWidths = calculateFixedEdgeWidths(ninePatchInfo, totalWidthPx)
	val middleSectionWidthPx = calculateMiddleSectionWidth(totalWidthPx, fixedEdgeWidths)
	val middleSectionWidthDp = with(density) { (middleSectionWidthPx / density.density).toDp() }
	val useHorizontalLayout = middleSectionWidthDp.value >= MIN_HORIZONTAL_LAYOUT_WIDTH_DP
	
	return LayoutDecision(useHorizontalLayout = useHorizontalLayout)
}

private fun calculateMiddleSectionWidth(
	totalWidthPx: Float,
	fixedEdgeWidths: FixedEdgeWidths
): Float {
	return totalWidthPx - fixedEdgeWidths.left - fixedEdgeWidths.right
}

private fun calculateFixedEdgeWidths(
	ninePatchInfo: NinePatchInfo?,
	totalWidthPx: Float
): FixedEdgeWidths {
	if (ninePatchInfo == null) {
		return FixedEdgeWidths(left = 0, right = 0)
	}
	
	val estimatedEdgeWidth = estimateFixedEdgeWidth(ninePatchInfo.intrinsicWidthPx)
	val maxEdgeWidth = calculateMaxEdgeWidth(totalWidthPx)
	
	val leftEdge = minOf(estimatedEdgeWidth, maxEdgeWidth)
	val rightEdge = minOf(estimatedEdgeWidth, maxEdgeWidth)
	
	return FixedEdgeWidths(left = leftEdge, right = rightEdge)
}

private fun estimateFixedEdgeWidth(intrinsicWidthPx: Int): Int {
	return (intrinsicWidthPx * FIXED_EDGE_WIDTH_PERCENTAGE)
		.toInt()
		.coerceIn(MIN_FIXED_EDGE_WIDTH_PX, MAX_FIXED_EDGE_WIDTH_PX)
}

private fun calculateMaxEdgeWidth(totalWidthPx: Float): Int {
	return (totalWidthPx * MAX_FIXED_EDGE_PERCENTAGE_OF_TOTAL).toInt()
}


@Composable
private fun ResourceContentLayout(
	state: ResourceBarState.Content,
	useHorizontalLayout: Boolean
) {
	if (useHorizontalLayout) {
		HorizontalResourceLayout(state)
	} else {
		VerticalResourceLayout(state)
	}
}

@Composable
private fun HorizontalResourceLayout(state: ResourceBarState.Content) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically,
	) {
		ResourceStateRouter(state.runeState, Modifier)
		Spacer(Modifier.width(HORIZONTAL_RESOURCE_SPACING_DP.dp))
		ResourceStateRouter(state.glyphState, Modifier)
	}
}

@Composable
private fun VerticalResourceLayout(state: ResourceBarState.Content) {
	val resourceCount = countResources(state)
	val hasMultipleResources = resourceCount > 1
	
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		ResourceStateRouter(state.runeState, Modifier, isVerticalLayout = true, hasMultipleResources = hasMultipleResources)
		if (hasMultipleResources) {
			Spacer(Modifier.height(VERTICAL_RESOURCE_SPACING_DP.dp))
		}
		ResourceStateRouter(state.glyphState, Modifier, isVerticalLayout = true, hasMultipleResources = hasMultipleResources)
	}
}

private fun getNinePatchInfo(context: Context, drawableRes: Int): NinePatchInfo? {
	val drawable = ContextCompat.getDrawable(context, drawableRes)
	return if (drawable is NinePatchDrawable) {
		val padding = Rect()
		drawable.getPadding(padding)
		NinePatchInfo(
			intrinsicWidthPx = drawable.intrinsicWidth,
			originalPadding = padding
		)
	} else {
		null
	}
}

@Composable
private fun ResourceStateRouter(
	state: ResourceState,
	modifier: Modifier = Modifier,
	isVerticalLayout: Boolean = false,
	hasMultipleResources: Boolean = false
) {
	when(state) {
		is ResourceState.Content -> ResourceStateContent(
			state = state,
			modifier = modifier,
			isVerticalLayout = isVerticalLayout,
			hasMultipleResources = hasMultipleResources
		)
		is ResourceState.None -> Unit
	}
}

@Composable
private fun ResourceStateContent(
	state: ResourceState.Content,
	modifier: Modifier = Modifier,
	isVerticalLayout: Boolean = false,
	hasMultipleResources: Boolean = false
) {
	val iconSize = iconSizeRouter(isVerticalLayout)
	val textStyle = textStyleRouter(isVerticalLayout, hasMultipleResources)
	
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
	) {
		ImageRouter(
			state.icon,
			modifier = Modifier.size(iconSize)
		)
		Text(
			modifier = Modifier,
			maxLines = 1,
			text = state.amount,
			style = textStyle
		)
	}
}

private fun iconSizeRouter(isVerticalLayout: Boolean): Dp {
	return if (isVerticalLayout) {
		VERTICAL_ICON_SIZE_DP.dp
	} else {
		HORIZONTAL_ICON_SIZE_DP.dp
	}
}

private fun textStyleRouter(
	isVerticalLayout: Boolean,
	hasMultipleResources: Boolean
): androidx.compose.ui.text.TextStyle {
	return when {
		isVerticalLayout && hasMultipleResources -> Typography.bodySmall
		else -> Typography.bodyMedium
	}
}

private fun countResources(state: ResourceBarState.Content): Int {
	var count = 0
	if (state.runeState is ResourceState.Content) count++
	if (state.glyphState is ResourceState.Content) count++
	return count
}

@Composable
private fun resourceBarMaxWidthRouter(): Dp {
	return when (DeviceUtil.isLandscape) {
		true -> DeviceUtil.getColumnWidthDp(8)
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
