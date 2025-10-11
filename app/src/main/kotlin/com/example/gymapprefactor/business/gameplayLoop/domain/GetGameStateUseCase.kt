package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.GameState

class GetGameStateUseCase(override val repository: GameplayRepository) : UseCase {
	suspend operator fun invoke(): GameState {
		return repository.getGameState()
	}
}

class InitializeGameUseCase(override val repository: GameplayRepository) : UseCase {
	// create a new GameState, save it to the app data model, return it
}
