package com.example.gymapprefactor.common.components.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.FoldingButtonState
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState

@Composable
fun ButtonRouter(
    state: ButtonState,
    modifier: Modifier = Modifier,
) {
    when(state) {
        is FoldingButtonState -> FoldingButtonRouter(state, modifier)
        is RectangleButtonState -> RectangleButtonRouter(state, modifier)
    }
}
