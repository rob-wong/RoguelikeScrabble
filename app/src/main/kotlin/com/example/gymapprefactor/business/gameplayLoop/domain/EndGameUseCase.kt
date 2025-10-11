package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.ActiveGameState

class EndGameUseCase(override val repository: GameplayRepository): UseCase {
	suspend operator fun invoke(game: ActiveGameState, saveProgression: Boolean) {
		repository.endGame(game, saveProgression)
	}
}
