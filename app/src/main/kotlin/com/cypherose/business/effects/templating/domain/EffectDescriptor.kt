package com.cypherose.business.effects.templating.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class EffectDescriptor(
	val type: String,
	val config: JsonObject
)
