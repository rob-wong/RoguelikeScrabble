package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.ActiveGameState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOptionType
import javax.inject.Inject

interface MidshopOptionMapper : Mapper<MidshopOptionMapper.Param, List<MidshopOption>> {
	data class Param(
		val game: ActiveGameState
	)
}

class MidshopOptionMapperImpl @Inject constructor() : MidshopOptionMapper {
	override fun map(param: MidshopOptionMapper.Param): List<MidshopOption> {
		return listOf(
			MidshopOption(
				id = "midshop_upgrade",
				cost = 0,
				type = MidshopOptionType.Upgrade
			),
			MidshopOption(
				id = "midshop_awaken",
				cost = 2,
				type = MidshopOptionType.Awaken
			),
			MidshopOption(
				id = "midshop_expunge",
				cost = 5,
				type = MidshopOptionType.Expunge
			)
		)
	}
}
