package com.example.gymapprefactor.business.gameplayLoop.domain.usecases

import com.example.gymapprefactor.business.gameplayLoop.domain.GameplayRepository
import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.GameState

class GetGameStateUseCase(override val repository: GameplayRepository) : UseCase {
	suspend operator fun invoke(): GameState {
		return repository.getGameState()
	}
}
