package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.gymapprefactor.common.components.presentation.SelectorState
import com.example.gymapprefactor.common.components.ui.previews.SelectorProvider

@Composable
fun Selector (
    state: SelectorState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(onClick = { }) {
            Text(text = when (state) {
                is SelectorState.None -> "herro"
                else -> "whatsUp"
            })
        }
    }
}

@Preview
@Composable
internal fun SelectorPreview(
    @PreviewParameter(SelectorProvider::class) state: SelectorState
) {
    Selector(state)
}
