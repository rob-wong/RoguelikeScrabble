package com.example.gymapprefactor.features.upgrade.presentation.state

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.features.upgrade.domain.UpgradeCostMapper
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState
import javax.inject.Inject

interface UpgradeLetterStateMapper : Mapper<UpgradeLetterStateMapper.Param, UpgradeLetterState> {
	data class Param(
		val deckType: DeckType,
		val letter: Letter,
		val runesCount: Int,
		val upgradeCostMapper: UpgradeCostMapper,
		val onLetterClick: (cost: Int) -> Unit,
	)
}

class UpgradeLetterStateMapperImpl @Inject constructor() : UpgradeLetterStateMapper {
	override fun map(param: UpgradeLetterStateMapper.Param): UpgradeLetterState {
		with(param) {
			val letterState = LetterState.Display(
				type = deckType,
				letter = letter.letter.toUpperCase(),
				level = letter.level
			)
			val cost = upgradeCostMapper.map(letter.level)
			val canAfford = runesCount >= cost
			val isClickable = canAfford && letter.level < UpgradeCostMapper.MAX_LETTER_LEVEL

			return UpgradeLetterState.Content(
				letterState = letterState,
				cost = cost,
				canAfford = canAfford,
				isClickable = isClickable,
				onLetterClick = { onLetterClick(cost) }
			)
		}
	}
}
