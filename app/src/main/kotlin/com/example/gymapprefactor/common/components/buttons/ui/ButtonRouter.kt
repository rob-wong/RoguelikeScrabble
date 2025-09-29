package com.example.gymapprefactor.common.components.buttons.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.ImageButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.FoldingButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.RectangleButtonState

@Composable
fun ButtonRouter(
    state: ButtonState,
    modifier: Modifier = Modifier,
) {
    when(state) {
        is FoldingButtonState -> FoldingButtonRouter(state, modifier)
        is RectangleButtonState -> RectangleButtonRouter(state, modifier)
        is IconButtonState -> IconButtonRouter(state, modifier)
        is ImageButtonState -> ImageButtonRouter(state, modifier)
    }
}
