package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.GameState

class SaveGameStateUseCase(override val repository: GameplayRepository) : UseCase {
	suspend operator fun invoke(game: GameState): GameState {
		return repository.saveGameState(game = game)
	}
}
