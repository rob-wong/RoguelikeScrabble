package com.example.gymapprefactor.business.models

interface Effect {
	val id: String
	val label: String
}

class DefaultEffect(
	override val id: String,
	override val label: String
) : Effect
