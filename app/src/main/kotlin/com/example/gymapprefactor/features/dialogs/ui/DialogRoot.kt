package com.example.gymapprefactor.features.dialogs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.common.components.buttons.presentation.IconButtonState
import com.example.gymapprefactor.common.components.buttons.ui.ButtonRouter
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ImageRouter
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogState
import com.example.gymapprefactor.features.dialogs.presentation.viewmodel.DialogViewModelImpl
import com.example.gymapprefactor.ui.theme.Typography

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
		Box(
			modifier = modifier.wrapContentSize(),
			contentAlignment = Alignment.TopCenter
		) {
			ImageRouter(
				state = ImageState.DialogBackground,
				modifier = Modifier
					.width(dialogWidthRouter()),
				isLandscape = DeviceUtil.isLandscape,
				contentScale = ContentScale.FillWidth
			)
			Column(
				modifier = Modifier.padding(100.dp)
			) {
				Text(
					text = state.title,
					style = Typography.bodyMedium,
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center
				)
				if (state.message != null) {
					Text(
						text = state.message,
						style = Typography.bodySmall,
						modifier = Modifier.fillMaxWidth(),
						textAlign = TextAlign.Center
					)
				}
				ButtonRouter(state.confirmState, Modifier.fillMaxWidth())
				ButtonRouter(state.dismissState, Modifier.fillMaxWidth())
			}
		}
	}
}

@Composable
private fun dialogWidthRouter(): Dp {
	with (DeviceUtil) {
		return when (isLandscape) {
			true -> getColumnWidthDp(6)
			false -> getColumnWidthDp(8)
		}
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
			confirmState = IconButtonState.Content(
				onClick = { },
				image = ImageState.ConfirmIcon
			),
			dismissState = IconButtonState.Content(
				onClick = { },
				image = ImageState.DismissIcon
			),
		))
}
