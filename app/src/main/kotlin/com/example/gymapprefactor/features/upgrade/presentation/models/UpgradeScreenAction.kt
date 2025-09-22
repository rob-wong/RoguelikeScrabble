package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.business.interfaces.Action

sealed class UpgradeScreenAction : Action {
	data class SetContent(
		val runesCount: Int,
		val onBackPressed: () -> Unit
	) : UpgradeScreenAction()

	data object None : UpgradeScreenAction()
}
