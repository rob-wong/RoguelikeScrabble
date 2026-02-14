package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.business.interfaces.Action
import com.example.gymapprefactor.business.models.Letter

sealed class UpgradeScreenAction : Action {
	data class SetContent(
		val upgradeLetters: List<UpgradeLetterState>,
		val runesCount: Int,
		val onBackPressed: () -> Unit
	) : UpgradeScreenAction()

	data class SelectLetter(
		val letter: Letter,
		val cost: Int,
		val canAfford: Boolean,
		val onUpgrade: () -> Unit,
		val onDismiss: () -> Unit
	) : UpgradeScreenAction()

	data object DismissLetterSelection : UpgradeScreenAction()

	data class ShowUpgradeAnimation(
		val payload: UpgradeAnimationPayload
	) : UpgradeScreenAction()

	data object ClearUpgradeAnimation : UpgradeScreenAction()

	data object None : UpgradeScreenAction()
}
