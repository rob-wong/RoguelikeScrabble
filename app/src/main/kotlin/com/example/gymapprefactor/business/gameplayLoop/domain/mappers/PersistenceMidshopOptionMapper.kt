package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.ActiveGameState
import javax.inject.Inject

data class PersistenceMidshopResult(
	val gameState: ActiveGameState,
	val oldMaxRounds: Int,
	val newMaxRounds: Int
)

interface PersistenceMidshopOptionMapper :
	Mapper<PersistenceMidshopOptionMapper.Param, PersistenceMidshopResult> {
	data class Param(
		val game: ActiveGameState
	)
}

class PersistenceMidshopOptionMapperImpl @Inject constructor() : PersistenceMidshopOptionMapper {
	companion object {
		private const val ROUND_INCREASE = 1
	}

	override fun map(param: PersistenceMidshopOptionMapper.Param): PersistenceMidshopResult {
		val oldMaxRounds = param.game.activeGameVariables.maxRounds
		val newMaxRounds = oldMaxRounds + ROUND_INCREASE
		
		val updatedGameState = param.game.copy(
			activeGameVariables = param.game.activeGameVariables.copy(
				maxRounds = newMaxRounds
			)
		)
		
		return PersistenceMidshopResult(
			gameState = updatedGameState,
			oldMaxRounds = oldMaxRounds,
			newMaxRounds = newMaxRounds
		)
	}
}

