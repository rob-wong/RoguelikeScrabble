package com.example.gymapprefactor.features.upgrade.presentation.state

import com.example.gymapprefactor.common.components.presentation.ButtonState
import com.example.gymapprefactor.common.components.presentation.IconButtonState
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.ResourceBarState
import com.example.gymapprefactor.common.components.presentation.ResourceState
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenAction
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class UpgradeScreenReducerImpl : UpgradeScreenReducer {
	override val state = MutableStateFlow<UpgradeScreenState>(UpgradeScreenState.None)

	override suspend fun update(action: UpgradeScreenAction) {
		state.value = when(action) {
			is UpgradeScreenAction.SetContent -> mapContent(action)
			is UpgradeScreenAction.None -> UpgradeScreenState.None
		}
	}

	private fun mapContent(action: UpgradeScreenAction.SetContent): UpgradeScreenState {
		return UpgradeScreenState.Content(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceState.Content(
					amount = action.runesCount.toString(),
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceState.None
			),
			backButton = mapBackButton(action)
		)
	}

	private fun mapBackButton(action: UpgradeScreenAction.SetContent): ButtonState {
		return IconButtonState.Content(
			onClick = action.onBackPressed,
			image = ImageState.BackIcon
		)
	}
}
