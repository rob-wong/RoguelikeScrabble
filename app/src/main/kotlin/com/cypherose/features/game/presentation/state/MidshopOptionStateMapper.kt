package com.cypherose.features.game.presentation.state

import com.cypherose.business.interfaces.Mapper
import com.cypherose.common.components.presentation.ImageState
import com.cypherose.features.game.presentation.models.midshop.MidshopOption
import com.cypherose.features.game.presentation.models.midshop.MidshopOptionState
import com.cypherose.features.game.presentation.models.midshop.MidshopOptionType

interface MidshopOptionStateMapper : Mapper<MidshopOption, MidshopOptionState>

class MidshopOptionStateMapperImpl : MidshopOptionStateMapper {
	override fun map(param: MidshopOption): MidshopOptionState {
		val imageState = mapImageState(param.type)
		val description = mapDescription(param.type)
		return MidshopOptionState(
			option = param,
			imageState = imageState,
			description = description
		)
	}

	private fun mapImageState(type: MidshopOptionType): ImageState {
		return when (type) {
			is MidshopOptionType.Upgrade -> ImageState.Basic.UpgradeMidshopCard
			is MidshopOptionType.Awaken -> ImageState.Basic.AwakenMidshopCard
			is MidshopOptionType.Expunge -> ImageState.Basic.ExpungeMidshopCard
			is MidshopOptionType.Perfectionism -> ImageState.Basic.PerfectionismMidshopCard
			is MidshopOptionType.Persistence -> ImageState.Basic.PersistenceMidshopCard
		}
	}

	private fun mapDescription(type: MidshopOptionType): String {
		return when (type) {
			is MidshopOptionType.Upgrade -> "Upgrade 3 random letters from the bag. " +
				"If all letters are maxed, gain 1 Glyph per failed upgrade attempt"
			is MidshopOptionType.Awaken -> "Add 1 of 5 random letters to the bag"
			is MidshopOptionType.Expunge -> "Delete 1 of 5 randomly selected letters from the bag"
			is MidshopOptionType.Perfectionism -> "+1 Max Discard"
			is MidshopOptionType.Persistence -> "+1 Max Rounds"
		}
	}
}
