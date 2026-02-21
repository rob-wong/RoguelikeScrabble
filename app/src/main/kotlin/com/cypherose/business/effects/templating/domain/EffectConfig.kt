package com.cypherose.business.effects.templating.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
sealed interface EffectConfig

@Serializable
@SerialName("fixed_addition")
data class FixedAdditionConfig(
	val value: Int
) : EffectConfig

@Serializable
@SerialName("multiplication")
data class MultiplicationConfig(
	val multiplier: Double? = null,
	val type: String? = null,
	val minMultiplier: Double? = null,
	val maxMultiplier: Double? = null
) : EffectConfig

@Serializable
@SerialName("combo")
data class ComboConfig(
	val defaultEffect: EffectDescriptor,
	val condition: ComboCondition,
	val comboEffect: EffectDescriptor
) : EffectConfig

@Serializable
@SerialName("monetary")
data class MonetaryConfig(
	val type: String,
	val value: Int
) : EffectConfig
