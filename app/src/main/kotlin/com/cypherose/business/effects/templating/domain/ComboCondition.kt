package com.cypherose.business.effects.templating.domain

import com.cypherose.business.models.Effect
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
sealed interface ComboCondition {
	fun matches(effect: Effect): Boolean
}

@Serializable
@SerialName("label_matches")
data class LabelMatchesCondition(
	val value: String
) : ComboCondition {
	override fun matches(effect: Effect): Boolean {
		return effect.label.equals(value, ignoreCase = true)
	}
}

@Serializable
@SerialName("type_matches")
data class TypeMatchesCondition(
	val value: String
) : ComboCondition {
	override fun matches(effect: Effect): Boolean {
		// This will check if the effect's descriptor type matches
		// For now, we'll need to check this during processing
		return false // Will be handled in processor
	}
}
