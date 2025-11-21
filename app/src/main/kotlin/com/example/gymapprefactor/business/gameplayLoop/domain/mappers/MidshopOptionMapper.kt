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
		val currentGlyphCount = param.game.activeGameVariables.glyphCount
		
		return listOf(
			MidshopOption(
				id = "midshop_upgrade",
				cost = 0,
				type = MidshopOptionType.Upgrade,
				isEnabled = currentGlyphCount >= 0
			),
			MidshopOption(
				id = "midshop_awaken",
				cost = 2,
				type = MidshopOptionType.Awaken,
				isEnabled = currentGlyphCount >= 2
			),
			MidshopOption(
				id = "midshop_expunge",
				cost = 5,
				type = MidshopOptionType.Expunge,
				isEnabled = currentGlyphCount >= 5
			),
			MidshopOption(
				id = "midshop_perfectionism",
				cost = 8,
				type = MidshopOptionType.Perfectionism,
				isEnabled = currentGlyphCount >= 8
			),
			MidshopOption(
				id = "midshop_persistence",
				cost = 10,
				type = MidshopOptionType.Persistence,
				isEnabled = currentGlyphCount >= 10
			)
		)
	}
}
