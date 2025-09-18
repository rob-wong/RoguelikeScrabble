package com.example.gymapprefactor.common.components.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.app.util.SpacerUtil
import com.example.gymapprefactor.common.components.presentation.FoldingButtonState
import com.example.gymapprefactor.common.components.ui.previews.FoldingButtonProvider
import com.example.gymapprefactor.ui.theme.BlackishGray
import com.example.gymapprefactor.ui.theme.OffWhite

@Composable
fun FoldingButtonRouter(
	state: FoldingButtonState,
	modifier: Modifier = Modifier,
) {
	when (state) {
		is FoldingButtonState.Content -> {
			FoldedBooleanRouter(state, modifier)
		}

		is FoldingButtonState.None -> Unit
	}
}

@Composable
private fun FoldedBooleanRouter(
	state: FoldingButtonState.Content,
	modifier: Modifier,
) {
	var foldToggled by remember(state.text) { mutableStateOf(false) }
	FoldingButtonContent(
		modifier = modifier,
		state = state,
		foldToggled = foldToggled,
		onToggle = { foldToggled = !foldToggled }
	)
}

@Composable
private fun FoldingButtonContent(
	modifier: Modifier = Modifier,
	state: FoldingButtonState.Content,
	foldToggled: Boolean,
	onToggle: () -> Unit,
) {
	Row(
		horizontalArrangement = Arrangement.Start,
		modifier = modifier.fillMaxWidth(),
	) {
		Button(
			modifier = Modifier
                .foldingButtonWidth(
                    foldToggled = foldToggled,
                    buttonsSize = state.buttons.size * 1.0,
                    buttonWidthPercentage = state.buttonWidthPercentage,
                )
                .height(DeviceUtil.getColumnWidthDp(1))
                .animateContentSize(),
			shape = RectangleShape,
			colors = ButtonDefaults.buttonColors(
				containerColor = BlackishGray,
				contentColor = OffWhite,
			),
			border = BorderStroke(width = SpacerUtil.spacer_01, color = OffWhite),
			contentPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp),
			onClick = onToggle,
		) {
			Text(
				text = state.text
			)
		}

		state.buttons.forEachIndexed { index, button ->
			Spacer(Modifier.width(SpacerUtil.spacer_04))
			RectangleButtonRouter(
				button,
				modifier = Modifier.fillMaxWidth(getExpandRatio(index, state.buttons.size))
			)
		}
	}
}

@Composable
private fun Modifier.foldingButtonWidth(
	foldToggled: Boolean,
	buttonsSize: Double,
	buttonWidthPercentage: Float,
): Modifier {
	val animatedFraction by animateFloatAsState(
		targetValue = if (foldToggled) {
			getFoldRatio(buttonWidthPercentage, buttonsSize).toFloat()
		} else 1f,
		animationSpec = tween(durationMillis = 300)
	)
	return this.fillMaxWidth(animatedFraction)
}

private fun getExpandRatio(index: Int, listSize: Int): Float {
	return (index + 1).toFloat() / listSize
}

private fun getFoldRatio(buttonWidthPercentage: Float, numberOfButtons: Double): Double {
	return 1 - (buttonWidthPercentage * numberOfButtons)
}

@Preview
@Composable
internal fun FoldingButtonPreview(
    @PreviewParameter(FoldingButtonProvider::class) state: FoldingButtonState.Content,
) {
	FoldingButtonContent(
		state = state,
		modifier = Modifier,
		foldToggled = true,
		onToggle = { }
	)
}
