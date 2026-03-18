package com.cypherose.features.dialogs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cypherose.app.util.DevicePreviews
import com.cypherose.app.util.DeviceUtil
import com.cypherose.app.util.SpacerUtil
import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.buttons.ui.ButtonRouter
import com.cypherose.common.components.ui.ImageRouter
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.features.dialogs.presentation.models.DialogState
import com.cypherose.features.dialogs.presentation.viewmodel.DialogViewModelImpl
import com.cypherose.features.templateengine.ui.ContentRouter
import com.cypherose.ui.theme.Typography

@Composable
fun DialogRoot(
	modifier: Modifier = Modifier,
	viewModel: DialogViewModelImpl = hiltViewModel()
) {
	val state: DialogState by viewModel.state.collectAsStateWithLifecycle(DialogState.None)

	when (val screenState = state) {
		is DialogState.Content -> DialogContent(screenState, modifier)
		is DialogState.None -> Unit
	}
}

@Composable
private fun DialogContent(
	state: DialogState.Content,
	modifier: Modifier = Modifier,
) {
	Dialog(
		onDismissRequest = state.onDismissRequest,
	) {
		val dialogWidth = calculateDialogWidth()
		val dialogHeight = calculateDialogHeight()
		val dialogPadding = calculateDialogPadding()

		Box(
			modifier = modifier
				.width(dialogWidth)
				.height(dialogHeight),
			contentAlignment = Alignment.TopCenter
		) {
			ImageRouter(
				state = ImageState.NinePatch.DialogBackground,
				modifier = Modifier.fillMaxSize(),
				heightDp = dialogHeight,
			)
			
			// Content
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(dialogPadding)
			) {
				item {
					DialogTitle(state.title)
				}
				state.message?.let { msg ->
					item {
						Spacer(modifier = Modifier.height(SpacerUtil.spacer_04))
						DialogMessage(msg)
					}
				}

				items(state.customContent) { contentState ->
					Spacer(modifier = Modifier.height(SpacerUtil.spacer_04))
					ContentRouter(contentState, Modifier.fillMaxWidth())
				}
				item {
					Spacer(modifier = Modifier.height(SpacerUtil.spacer_20))
					ButtonRouter(state.confirmState, Modifier.fillMaxWidth())
					ButtonRouter(state.dismissState, Modifier.fillMaxWidth())
				}
			}
		}
	}
}

@Composable
private fun DialogTitle(title: String) {
	Text(
		text = title,
		style = Typography.bodyMedium,
		modifier = Modifier.fillMaxWidth(),
		textAlign = TextAlign.Center
	)
}

@Composable
private fun DialogMessage(message: String) {
	Text(
		text = message,
		style = Typography.bodySmall,
		modifier = Modifier.fillMaxWidth(),
		textAlign = TextAlign.Center
	)
}

@Composable
private fun calculateDialogWidth(): Dp {
	return when (DeviceUtil.isLandscape) {
		true -> DeviceUtil.getColumnWidthDp(8)
		false -> DeviceUtil.getColumnWidthDp(8)
	}
}

@Composable
private fun calculateDialogPadding(): PaddingValues {
	return PaddingValues(
		horizontal = when (DeviceUtil.isLandscape) {
			true -> DeviceUtil.getColumnWidthDp(1)
			false -> (DeviceUtil.getColumnWidthDp(3)/2)
		},
		vertical = 30.dp,
	)
}

@Composable
private fun calculateDialogHeight(): Dp {
	return when (DeviceUtil.isLandscape) {
		true -> DeviceUtil.getColumnWidthDp(6)
		false -> DeviceUtil.getColumnWidthDp(6)
	}
}

@Composable
@DevicePreviews
private fun DialogPreview() {
	DialogContent(
		DialogState.Content(
			onDismissRequest = { },
			title = "DIALOG TITLE",
			message = "Sample message",
			customContent = emptyList(),
			confirmState = IconButtonState.Content(
				onClick = { },
				image = ImageState.Basic.ConfirmIcon
			),
			dismissState = IconButtonState.Content(
				onClick = { },
				image = ImageState.Basic.DismissIcon
			),
		))
}
