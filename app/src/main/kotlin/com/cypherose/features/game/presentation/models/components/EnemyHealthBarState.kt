package com.cypherose.features.game.presentation.models.components

import com.cypherose.business.interfaces.State

sealed class EnemyHealthBarState : State {
	data class Content(
		val label: String,
		val currentHealth: Int,
		val maxHealth: Int,
	) : EnemyHealthBarState()

	data object None : EnemyHealthBarState()
}
