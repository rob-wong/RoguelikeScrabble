package com.example.gymapprefactor.business.models

interface Letter {
	val id: String
	val letter: Char
	val level: Int
}

class DefaultLetter(
	override val id: String,
	override val letter: Char,
	override val level: Int
) : Letter {
	override fun toString(): String {
		return "DefaultLetter($letter, level=$level)"
	}
}

fun Letter.copy(
	id: String = this.id,
	letter: Char = this.letter,
	level: Int = this.level
): Letter {
	return when (this) {
		is DefaultLetter -> DefaultLetter(
			id = id,
			letter = letter,
			level = level
		)

		else -> this
	}
}
