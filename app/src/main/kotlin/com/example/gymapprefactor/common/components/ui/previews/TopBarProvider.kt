package com.example.gymapprefactor.common.components.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.gymapprefactor.common.components.presentation.TopBarState

class TopBarProvider : PreviewParameterProvider<TopBarState.Content> {
    override val values = sequenceOf(
        TopBarState.Content(
            onBack = { },
            title = "title"
        )
    )
}
