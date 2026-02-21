package com.cypherose.common.components.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.cypherose.common.components.presentation.TopBarState

class TopBarProvider : PreviewParameterProvider<TopBarState.Content> {
    override val values = sequenceOf(
        TopBarState.Content(
            onBack = { },
            title = "title"
        )
    )
}
