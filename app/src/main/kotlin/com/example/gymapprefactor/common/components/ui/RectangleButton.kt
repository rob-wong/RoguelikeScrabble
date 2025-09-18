package com.example.gymapprefactor.common.components.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.ui.theme.BlackishGray
import com.example.gymapprefactor.ui.theme.OffWhite

@Composable
fun RectangleButtonRouter(
    state: RectangleButtonState,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = BlackishGray,
        contentColor = OffWhite,
    ),
) {
    when (state) {
        is RectangleButtonState.Content -> RectangularButton(
            onClick = state.onClick,
            text = state.text,
            modifier = modifier,
            colors = colors
        )
        is RectangleButtonState.None -> Unit
    }
}

@Composable
private fun RectangularButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = BlackishGray,
        contentColor = OffWhite,
    ),
    text: String
) {
    Button(
        onClick = onClick,
        border = BorderStroke(width = SpacerUtil.spacer_01, color = OffWhite),
        modifier = modifier
            .fillMaxWidth()
            .height(DeviceUtil.getColumnWidthDp(1)),
        shape = RectangleShape,
        colors = colors,
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            maxLines = 1
//            style = TextStyle(fontFamily = FontFamily(Font(R.font.noto_sc))),
        )
    }
}

@Preview
@Composable
fun RectangularButtonPreview() {
    RectangularButton(onClick = { }, text = "Button")
}
