package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.models.ActiveGameState
import com.cypherose.business.models.Effect
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

