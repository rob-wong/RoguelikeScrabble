package com.example.gymapprefactor.business.models

interface Letter {
	val id: String
}

class DefaultLetter(
	override val id: String
) : Letter
