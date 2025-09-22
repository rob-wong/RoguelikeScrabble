package com.example.gymapprefactor.features.dialogs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapprefactor.app.util.DevicePreviews
import com.example.gymapprefactor.app.util.DeviceUtil
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.ui.ImageRouter
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogState
import com.example.gymapprefactor.features.dialogs.presentation.viewmodel.DialogViewModelImpl

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
	modifier: Modifier = Modifier
) {
	Dialog(
		onDismissRequest = state.onDismissRequest,
	) {
			Box(
				modifier = modifier.wrapContentSize()
			) {
				ImageRouter(
					state = ImageState.DialogBackground,
					modifier = Modifier
						.width(dialogWidthRouter()),
					isLandscape = DeviceUtil.isLandscape,
					contentScale = ContentScale.FillWidth
				)
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
	DialogContent(DialogState.Content({ }, ""))
}
