package com.cypherose.common.components.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.cypherose.common.components.presentation.SelectorState

class SelectorProvider : PreviewParameterProvider<SelectorState> {
    override val values = sequenceOf(
        SelectorState.Content(listOf())
    )
}
