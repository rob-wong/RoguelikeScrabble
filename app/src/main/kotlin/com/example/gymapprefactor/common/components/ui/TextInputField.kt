package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.ui.theme.BlackishGray
import com.example.gymapprefactor.ui.theme.OffWhite

@Composable
fun TextInputFieldRouter(
    modifier: Modifier = Modifier,
    state: TextInputFieldState,
) {
    when(state) {
        is TextInputFieldState.Content -> TextInputField(
            text = state.text,
            onValueChanged = state.onValueChanged,
            modifier = modifier,
            placeholder = state.placeholder,
            maxLines = state.maxLines,
        )
        is TextInputFieldState.None -> Unit
    }
}

@Composable
private fun TextInputField(
    text: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    maxLines: Int = 1
) {
    var inputText by remember { mutableStateOf(text) }

    TextField(
        value = inputText,
        onValueChange = {
            inputText = it
            onValueChanged(it)
        },
        placeholder = { Text(text = placeholder) },
        modifier = modifier
            .fillMaxWidth()
            .background(color = BlackishGray)
            .border(BorderStroke(width = SpacerUtil.spacer_01, color = OffWhite)),
        maxLines = maxLines,
        singleLine = maxLines == 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = OffWhite,
            unfocusedTextColor = OffWhite,
            cursorColor = OffWhite,
        )
    )
}

@Preview
@Composable
fun TextInputFieldPreview() {
    TextInputField(text = "Example Text", onValueChanged = { })
}
