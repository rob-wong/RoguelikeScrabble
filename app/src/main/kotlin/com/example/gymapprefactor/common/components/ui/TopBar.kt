package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.gymapprefactor.R
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.common.components.ui.previews.TopBarProvider
import com.example.gymapprefactor.ui.theme.OffWhite

@Composable
fun TopBarRouter(
    modifier: Modifier = Modifier,
    state: TopBarState,
) {
    when (state) {
        is TopBarState.Content -> TopBarContent(state = state, modifier = modifier)
        is TopBarState.None -> Unit
    }
}

@Composable
private fun TopBarContent(
    modifier: Modifier = Modifier,
    state: TopBarState.Content,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(DeviceUtil.getColumnWidthDp(2))
            .background(color = Color.DarkGray.copy(alpha = 0.33f)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(SpacerUtil.spacer_30))
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(horizontal = DeviceUtil.getColumnWidthDp(1)),
                horizontalAlignment = AbsoluteAlignment.Left
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_icon),
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = BACK_ICON_SCALE
                            scaleY = BACK_ICON_SCALE
                        }
                        .alpha(BACK_ICON_OPACITY)
                        .clickable(onClick = {
                            state.onBack()
                        }),
                    contentDescription = "Back",
                )
            }

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Text(state.title, color = OffWhite) }

            // find a better fucking solution for this when doing ui work lmao
            Column(
                modifier = Modifier.padding(horizontal = DeviceUtil.getColumnWidthDp(1)),
                horizontalAlignment = AbsoluteAlignment.Right
            ) { Text("hello00", color = OffWhite, modifier = Modifier.alpha(0f)) }
        }
    }
}

const val BACK_ICON_SCALE = 0.2f
const val BACK_ICON_OPACITY = 0.7f

@Composable
@Preview
fun TopBarPreview(
    @PreviewParameter(TopBarProvider::class) state: TopBarState.Content
) {
    TopBarContent(state = state)
}
