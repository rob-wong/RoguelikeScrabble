package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.business.models.Effect
import javax.inject.Inject

class AddEffectToActiveGameValuesUseCase @Inject constructor() {
	operator fun invoke(
		effect: Effect,
		game: ActiveGameState
	): ActiveGameState {
		return game.copy(
			activeGameValues = game.activeGameValues.copy(
				effects = game.activeGameValues.effects + effect
			)
		)
	}
}

