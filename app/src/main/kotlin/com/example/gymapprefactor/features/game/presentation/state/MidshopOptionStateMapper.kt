package com.example.gymapprefactor.features.game.presentation.state

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOption
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOptionState
import com.example.gymapprefactor.features.game.presentation.models.midshop.MidshopOptionType

interface MidshopOptionStateMapper : Mapper<MidshopOption, MidshopOptionState>

class MidshopOptionStateMapperImpl : MidshopOptionStateMapper {
	override fun map(param: MidshopOption): MidshopOptionState {
		val imageState = mapImageState(param.type)
		return MidshopOptionState(
			option = param,
			imageState = imageState
		)
	}

	private fun mapImageState(type: MidshopOptionType): ImageState {
		return when (type) {
			is MidshopOptionType.Upgrade -> ImageState.UpgradeMidshopCard
			is MidshopOptionType.Awaken -> ImageState.AwakenMidshopCard
			is MidshopOptionType.Expunge -> ImageState.ExpungeMidshopCard
			is MidshopOptionType.Perfectionism -> ImageState.PerfectionismMidshopCard
			is MidshopOptionType.Persistence -> ImageState.PersistenceMidshopCard
		}
	}
}
