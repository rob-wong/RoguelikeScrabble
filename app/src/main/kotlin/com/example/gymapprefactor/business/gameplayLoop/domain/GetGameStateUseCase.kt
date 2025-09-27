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

class EndGameUseCase(override val repository: GameplayRepository): UseCase {
	// pass a GameState in the invoke parameters,
	// check if it's active, if so add the runes to the user in appdatamodel
	// then save a None game state
}
