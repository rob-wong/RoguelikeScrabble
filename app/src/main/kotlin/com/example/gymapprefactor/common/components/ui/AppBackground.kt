package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.ui.theme.BackgroundOrange
import com.example.gymapprefactor.ui.theme.BackgroundRed

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .backgroundGradient()
            .backgroundVignette()
    ) {
        content()
    }
}

private fun Modifier.backgroundGradient() = this.background(
    brush = Brush.radialGradient(
        colors = listOf(BackgroundOrange, BackgroundRed),
        center = gradientCornerRouter(),
        radius = DeviceUtil.getColumnWidthPx(backgroundRadiusRouter())
    )

)

private fun Modifier.backgroundVignette() =
    this.background(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.2f),
                Color.Black // Edges
            ),
            center = Offset.Unspecified,
            radius = DeviceUtil.getColumnWidthPx(backgroundRadiusRouter())
        )

    )

private fun gradientCornerRouter(): Offset {
    return when (DeviceUtil.isLandscape) {
        true -> Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        false -> Offset(Float.POSITIVE_INFINITY, Float.MIN_VALUE)
    }
}

private fun backgroundRadiusRouter(): Int {
    return when (DeviceUtil.isLandscape) {
        true -> LANDSCAPE_BACKGROUND_COLUMN_RADIUS
        false -> PORTRAIT_BACKGROUND_COLUMN_RADIUS
    }
}

const val PORTRAIT_BACKGROUND_COLUMN_RADIUS = 60
const val LANDSCAPE_BACKGROUND_COLUMN_RADIUS = 40

@Preview
@Composable
internal fun BackgroundPreview() {
    AppBackground(Modifier) { }
}
