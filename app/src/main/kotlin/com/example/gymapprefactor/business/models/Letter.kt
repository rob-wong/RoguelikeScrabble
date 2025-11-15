package com.example.gymapprefactor.business.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

@Polymorphic
interface Letter {
	val id: String
	val letter: Char
	val level: Int
}

@Serializable
@SerialName("default")
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
