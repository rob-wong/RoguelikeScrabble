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
) : Letter
