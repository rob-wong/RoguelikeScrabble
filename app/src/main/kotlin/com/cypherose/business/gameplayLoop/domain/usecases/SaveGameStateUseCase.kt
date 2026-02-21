package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.models.GameState

class SaveGameStateUseCase(override val repository: GameplayRepository) : UseCase {
	suspend operator fun invoke(game: GameState): GameState {
		return repository.saveGameState(game = game)
	}
}
