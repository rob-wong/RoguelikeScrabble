package com.example.gymapprefactor.features.game.presentation.models.components

import com.example.gymapprefactor.business.interfaces.State

sealed class EnemyHealthBarState : State {
	data class Content(
		val label: String,
		val currentHealth: Int,
		val maxHealth: Int,
	) : EnemyHealthBarState()

	data object None : EnemyHealthBarState()
}
