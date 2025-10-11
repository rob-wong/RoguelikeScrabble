package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.business.models.Deck
import com.example.gymapprefactor.business.models.Letter
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapper.Param
import com.example.gymapprefactor.business.gameplayLoop.domain.DrawHandMapper.Output
import kotlin.random.Random

interface DrawHandMapper : Mapper<Param, Output> {
	data class Param(
		val deck: Deck,
		val seed: Long,
		val handSize: Int,
	)
	data class Output(
		val drawn: List<Letter>,
		val remaining: List<Letter>,
	)
}

class DrawHandMapperImpl : DrawHandMapper {
	override fun map(param: Param): Output {
		with (param) {
			if (deck.letters.isEmpty() || handSize <= 0) {
				return Output(emptyList(), deck.letters)
			}

			val random = Random(seed)
			val shuffled = deck.letters.shuffled(random)

			val drawCount = minOf(handSize, shuffled.size)
			val drawn = shuffled.take(drawCount)
			val remaining = shuffled.drop(drawCount)

			return Output(drawn, remaining)
		}
	}
}
