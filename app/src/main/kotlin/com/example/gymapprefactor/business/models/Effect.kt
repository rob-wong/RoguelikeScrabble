package com.example.gymapprefactor.business.models

import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName

@Polymorphic
interface Effect {
	val id: String
	val label: String
	val descriptor: EffectDescriptor?
}

@Serializable
@SerialName("default")
class DefaultEffect(
	override val id: String,
	override val label: String,
	override val descriptor: EffectDescriptor? = null
) : Effect

@Serializable
@SerialName("described")
class DescribedEffect(
	override val id: String,
	override val label: String,
	override val descriptor: EffectDescriptor
) : Effect
