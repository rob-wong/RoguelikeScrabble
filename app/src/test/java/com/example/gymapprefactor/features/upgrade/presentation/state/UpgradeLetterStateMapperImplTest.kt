package com.example.gymapprefactor.features.upgrade.presentation.state

import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.buttons.presentation.ImageButtonState
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class UpgradeLetterStateMapperImplTest {
	private val sut = UpgradeLetterStateMapperImpl()

	private val letter = mockk<Letter> {
		every { letter } returns 'a'
	}

	@Test
	fun `Given params, when map, then content expected`() = runTest {
		// Given
		every { letter.level } returns 3

		// When
		val result = sut.map(UpgradeLetterStateMapper.Param(
			deckType = deckType,
			letter = letter,
			onUpgrade = onUpgrade
		))

		// Then
		assertIs<UpgradeLetterState.Content>(result)
		assertIs<ImageButtonState.Content>(result.buttonState)
		assertIs<LetterState.Display>(result.letterState)

		val buttonState = result.buttonState as ImageButtonState.Content
		buttonState.onClick.invoke()

		verify { onUpgrade(letter) }
	}

	@Test
	fun `Given max letter, When map, Then none Button state expected`() = runTest {
		// Given
		every { letter.level } returns 5

		// When
		val result = sut.map(UpgradeLetterStateMapper.Param(
			deckType = deckType,
			letter = letter,
			onUpgrade = onUpgrade
		))

		// Then
		assertIs<UpgradeLetterState.Content>(result)
		assertIs<ImageButtonState.None>(result.buttonState)
	}

	private companion object {
		val deckType = DeckType.Default
		val onUpgrade: (Letter) -> Unit = spyk()
	}
}
