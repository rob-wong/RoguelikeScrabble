package com.cypherose.business.gameplayLoop.domain.usecases

import com.cypherose.business.gameplayLoop.domain.GameplayRepository
import com.cypherose.business.interfaces.UseCase

class AddPreviouslyPlayedEffectUseCase(override val repository: GameplayRepository) : UseCase {
	suspend operator fun invoke(label: String) {
		repository.addPreviouslyPlayedEffect(label)
	}
}
