package com.example.gymapprefactor.business.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

@Polymorphic
interface Effect {
	val id: String
	val label: String
}

@Serializable
@SerialName("default")
class DefaultEffect(
	override val id: String,
	override val label: String
) : Effect
