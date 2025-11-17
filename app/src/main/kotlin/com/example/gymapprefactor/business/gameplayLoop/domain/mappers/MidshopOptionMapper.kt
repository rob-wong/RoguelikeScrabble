package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.game.presentation.models.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.MidshopOptionType
import javax.inject.Inject

interface MidshopOptionMapper : Mapper<MidshopOptionMapper.Param, List<MidshopOption>> {
	data class Param(
		val game: ActiveGameState
	)
}

class MidshopOptionMapperImpl @Inject constructor() : MidshopOptionMapper {
	override fun map(param: MidshopOptionMapper.Param): List<MidshopOption> {
		// For now, return one free Upgrade option
		// This will be expanded to include other types in future PRs
		return listOf(
			MidshopOption(
				id = "midshop_upgrade",
				cost = 0,
				type = MidshopOptionType.Upgrade
			)
		)
	}
}
