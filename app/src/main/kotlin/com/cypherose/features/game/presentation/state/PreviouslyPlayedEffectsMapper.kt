package com.cypherose.features.game.presentation.state

import com.cypherose.business.interfaces.Mapper
import com.cypherose.business.models.Letter
import com.cypherose.features.game.presentation.models.PreviouslyPlayedEffectItem
import javax.inject.Inject

interface PreviouslyPlayedEffectsMapper :
	Mapper<PreviouslyPlayedEffectsMapper.Param, List<PreviouslyPlayedEffectItem>> {
	data class Param(
		val effectLabels: List<String>,
		val hand: List<Letter>
	)
}

class PreviouslyPlayedEffectsMapperImpl @Inject constructor() : PreviouslyPlayedEffectsMapper {
	override fun map(param: PreviouslyPlayedEffectsMapper.Param): List<PreviouslyPlayedEffectItem> {
		val handLetterCounts = computeHandLetterCounts(param.hand)
		return param.effectLabels
			.map { label -> toItem(label, handLetterCounts) }
			.sortedByDescending { it.matchPercent }
	}

	private fun computeHandLetterCounts(hand: List<Letter>): Map<Char, Int> {
		return hand.map { it.letter.uppercaseChar() }
			.groupingBy { it }
			.eachCount()
	}

	private fun toItem(label: String, handLetterCounts: Map<Char, Int>): PreviouslyPlayedEffectItem {
		val matchPercent = computeMatchPercent(label, handLetterCounts)
		return PreviouslyPlayedEffectItem(label = label, matchPercent = matchPercent)
	}

	private fun computeMatchPercent(label: String, handLetterCounts: Map<Char, Int>): Float {
		if (label.isEmpty()) return 0f
		val tempHandCounts = handLetterCounts.toMutableMap()
		var matchingCount = 0
		for (char in label.uppercase()) {
			val count = tempHandCounts[char] ?: 0
			if (count > 0) {
				matchingCount++
				tempHandCounts[char] = count - 1
			}
		}
		return matchingCount.toFloat() / label.length
	}
}
