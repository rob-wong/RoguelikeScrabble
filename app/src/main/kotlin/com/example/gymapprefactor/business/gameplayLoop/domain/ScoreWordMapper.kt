package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.ActiveGameValues
import com.example.gymapprefactor.business.models.Letter
import javax.inject.Inject

typealias LetterScore = Pair<String, Int>

interface ScoreWordMapper : Mapper<ScoreWordMapper.Param, List<LetterScore>> {
	data class Param(
		val letters: List<Letter>,
		val activeGameValues: ActiveGameValues,
	)
}

class ScoreWordMapperImpl @Inject constructor() : ScoreWordMapper {

	override fun map(param: ScoreWordMapper.Param): List<LetterScore> {
		with(param) {
			if (letters.isEmpty()) return emptyList()

			// TODO: replace with real deck effect multiplier when implemented
			val deckMultiplier = 1f

			return letters.map { letter ->
				val baseScore = 1
				val levelBonus = letter.level.coerceAtLeast(1)
				val letterScore = ((baseScore + levelBonus) * deckMultiplier).toInt()
				letter.id to letterScore
			}
		}
	}
}

