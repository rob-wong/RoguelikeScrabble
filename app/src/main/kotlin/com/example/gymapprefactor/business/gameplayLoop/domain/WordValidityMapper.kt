package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.Letter
import javax.inject.Inject

interface WordValidityMapper : Mapper<List<Letter>, Boolean>

class WordValidityMapperImpl @Inject constructor(
	private val validWords: Set<String>
) : WordValidityMapper {

	override fun map(param: List<Letter>): Boolean {
		if (param.size == 1) { return false }

		val word = mapLettersToWord(param)
		return validWords.contains(word)
	}

	private fun mapLettersToWord(letters: List<Letter>): String {
		return letters.map { it.letter }
			.joinToString("")
			.lowercase()
	}
}
