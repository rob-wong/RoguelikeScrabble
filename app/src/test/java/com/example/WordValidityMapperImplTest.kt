package com.example

import com.cypherose.business.gameplayLoop.domain.mappers.WordValidityMapperImpl
import com.cypherose.business.models.DefaultLetter
import com.cypherose.business.models.Letter
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WordValidityMapperImplTest {

	private val sut = WordValidityMapperImpl(validWords)

	@Test
	fun `Given valid word, When map, Then true expected`() = runTest {
		// Given
		val letters = listOf<Letter>(
			DefaultLetter("", 'v', 0),
			DefaultLetter("", 'a', 0),
			DefaultLetter("", 'l', 0),
			DefaultLetter("", 'i', 0),
			DefaultLetter("", 'd', 0),
		)

		// When
		val result = sut.map(letters)

		// Then
		assertTrue(result)
	}

	@Test
	fun `Given invalid word containing valid word, When map, Then false expected`() = runTest {
		// Given
		val letters = listOf<Letter>(
			DefaultLetter("", 'v', 0),
			DefaultLetter("", 'a', 0),
			DefaultLetter("", 'l', 0),
			DefaultLetter("", 'i', 0),
			DefaultLetter("", 'd', 0),
			DefaultLetter("", 'i', 0),
			DefaultLetter("", 't', 0),
			DefaultLetter("", 'y', 0),
		)

		// When
		val result = sut.map(letters)

		// Then
		assertFalse(result)
	}

	@Test
	fun `Given1 letter word, When map, Then false expected`() = runTest {
		// Given
		val letters = listOf<Letter>(
			DefaultLetter("", 'i', 0)
		)

		// When
		val result = sut.map(letters)

		// Then
		assertFalse(result)
	}

	private companion object {
		val validWords: Set<String> = setOf("valid", "i")
	}
}
