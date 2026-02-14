package com.example.gymapprefactor.features.upgrade.presentation.state

import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.common.components.presentation.DeckType
import com.example.gymapprefactor.common.components.presentation.LetterState
import com.example.gymapprefactor.features.upgrade.domain.UpgradeCostMapperImpl
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeLetterState
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UpgradeLetterStateMapperImplTest {
	private val sut = UpgradeLetterStateMapperImpl()
	private val upgradeCostMapper = UpgradeCostMapperImpl()

	private val letter = mockk<Letter> {
		every { letter } returns 'a'
	}

	@Test
	fun `Given params, when map, then content expected`() = runTest {
		every { letter.level } returns 3

		val result = sut.map(UpgradeLetterStateMapper.Param(
			deckType = deckType,
			letter = letter,
			runesCount = 10,
			upgradeCostMapper = upgradeCostMapper,
			onLetterClick = onLetterClick
		))

		assertIs<UpgradeLetterState.Content>(result)
		assertIs<LetterState.Display>(result.letterState)
		assertEquals(3, result.cost)
		assertTrue(result.canAfford)
		assertTrue(result.isClickable)

		result.onLetterClick(3)
		verify { onLetterClick(3) }
	}

	@Test
	fun `Given max letter, when map, then not clickable expected`() = runTest {
		every { letter.level } returns 5

		val result = sut.map(UpgradeLetterStateMapper.Param(
			deckType = deckType,
			letter = letter,
			runesCount = 10,
			upgradeCostMapper = upgradeCostMapper,
			onLetterClick = onLetterClick
		))

		assertIs<UpgradeLetterState.Content>(result)
		assertEquals(5, result.cost)
		assertTrue(result.canAfford)
		assertFalse(result.isClickable)
	}

	@Test
	fun `Given insufficient runes, when map, then not clickable expected`() = runTest {
		every { letter.level } returns 3

		val result = sut.map(UpgradeLetterStateMapper.Param(
			deckType = deckType,
			letter = letter,
			runesCount = 2,
			upgradeCostMapper = upgradeCostMapper,
			onLetterClick = onLetterClick
		))

		assertIs<UpgradeLetterState.Content>(result)
		assertEquals(3, result.cost)
		assertFalse(result.canAfford)
		assertFalse(result.isClickable)
	}

	private companion object {
		val deckType = DeckType.Default
		val onLetterClick: (Int) -> Unit = spyk()
	}
}
