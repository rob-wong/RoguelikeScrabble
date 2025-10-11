package com.example.gymapprefactor.features.upgrade.presentation.state

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.buttons.presentation.ButtonState
import com.example.gymapprefactor.common.components.buttons.presentation.ImageButtonState
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.ImageState
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState

interface UpgradeLetterStateMapper : Mapper<UpgradeLetterStateMapper.Param, UpgradeLetterState> {
	data class Param(
		val deckType: DeckType,
		val letter: Letter,
		val onUpgrade: (Letter) -> Unit,
	)
}

class UpgradeLetterStateMapperImpl : UpgradeLetterStateMapper {
	override fun map(param: UpgradeLetterStateMapper.Param): UpgradeLetterState {
		with(param) {
			val letterState = LetterState.Display(
				type = deckType,
				letter = letter.letter.toUpperCase(),
				level = letter.level
			)
			val buttonState = mapButton(param)

			return UpgradeLetterState.Content(
				letterState = letterState,
				buttonState = buttonState
			)
		}
	}

	private fun mapButton(param: UpgradeLetterStateMapper.Param): ButtonState {
		with (param) {
			return if (letter.level < MAX_LETTER_LEVEL) {
				ImageButtonState.Content(
					onClick = { onUpgrade(letter) },
					background = ImageState.UpgradeButton,
					foreground = ImageState.None
				)
			} else {
				ImageButtonState.None
			}
		}
	}
	private companion object {
		const val MAX_LETTER_LEVEL = 5
	}
}
