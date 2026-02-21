package com.cypherose.features.upgrade.presentation.state

import com.cypherose.common.components.buttons.presentation.IconButtonState
import com.cypherose.common.components.buttons.presentation.ButtonState
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.common.components.presentation.ResourceBarState
import com.cypherose.common.components.presentation.ResourceState
import com.cypherose.features.upgrade.presentation.models.SelectedLetterState
import com.cypherose.features.upgrade.presentation.models.UpgradeScreenAction
import com.cypherose.features.upgrade.presentation.models.UpgradeScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class UpgradeScreenReducerImpl : UpgradeScreenReducer {
	override val state = MutableStateFlow<UpgradeScreenState>(UpgradeScreenState.None)

	override suspend fun update(action: UpgradeScreenAction) {
		val currentState = state.value
		state.value = when(action) {
			is UpgradeScreenAction.SetContent -> mapContent(action)
			is UpgradeScreenAction.SelectLetter -> mapSelectLetter(currentState, action)
			is UpgradeScreenAction.DismissLetterSelection -> mapDismissLetterSelection(currentState)
			is UpgradeScreenAction.ShowUpgradeAnimation -> mapShowUpgradeAnimation(currentState, action)
			is UpgradeScreenAction.ClearUpgradeAnimation -> mapClearUpgradeAnimation(currentState)
			is UpgradeScreenAction.None -> UpgradeScreenState.None
		}
	}

	private fun mapContent(action: UpgradeScreenAction.SetContent): UpgradeScreenState {
		val currentState = state.value
		val existingAnimation = if (currentState is UpgradeScreenState.Content) {
			currentState.upgradeAnimation
		} else {
			null
		}
		val existingSelectedLetter = if (currentState is UpgradeScreenState.Content) {
			currentState.selectedLetter
		} else {
			null
		}
		
		return UpgradeScreenState.Content(
			resourceBar = ResourceBarState.Content(
				runeState = ResourceState.Content(
					amount = action.runesCount.toString(),
					icon = ImageState.RuneIcon
				),
				glyphState = ResourceState.None
			),
			backButton = mapBackButton(action),
			letters = action.upgradeLetters,
			selectedLetter = existingSelectedLetter,
			upgradeAnimation = existingAnimation
		)
	}

	private fun mapSelectLetter(
		currentState: UpgradeScreenState,
		action: UpgradeScreenAction.SelectLetter
	): UpgradeScreenState {
		if (currentState !is UpgradeScreenState.Content) return currentState
		
		val selectedLetter = SelectedLetterState(
			letter = action.letter,
			cost = action.cost,
			canAfford = action.canAfford,
			onUpgrade = action.onUpgrade,
			onDismiss = action.onDismiss
		)
		
		return currentState.copy(selectedLetter = selectedLetter)
	}

	private fun mapDismissLetterSelection(currentState: UpgradeScreenState): UpgradeScreenState {
		if (currentState !is UpgradeScreenState.Content) return currentState
		return currentState.copy(selectedLetter = null)
	}

	private fun mapShowUpgradeAnimation(
		currentState: UpgradeScreenState,
		action: UpgradeScreenAction.ShowUpgradeAnimation
	): UpgradeScreenState {
		if (currentState !is UpgradeScreenState.Content) return currentState
		return currentState.copy(upgradeAnimation = action.payload)
	}

	private fun mapClearUpgradeAnimation(currentState: UpgradeScreenState): UpgradeScreenState {
		if (currentState !is UpgradeScreenState.Content) return currentState
		return currentState.copy(upgradeAnimation = null)
	}

	private fun mapBackButton(action: UpgradeScreenAction.SetContent): ButtonState {
		return IconButtonState.Content(
			onClick = action.onBackPressed,
			image = ImageState.BackIcon
		)
	}
}
