package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.models.ActiveGameState

class EndGameUseCase(override val repository: GameplayRepository): UseCase {
	suspend operator fun invoke(game: ActiveGameState, saveProgression: Boolean) {
		repository.endGame(game, saveProgression)
	}
}
