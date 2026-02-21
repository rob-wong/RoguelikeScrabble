package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.models.GameState

class GetGameStateUseCase(override val repository: GameplayRepository) : UseCase {
	suspend operator fun invoke(): GameState {
		return repository.getGameState()
	}
}
