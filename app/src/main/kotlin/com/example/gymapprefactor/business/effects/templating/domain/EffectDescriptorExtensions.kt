package com.example.gymapprefactor.business.effects.templating.domain

import kotlinx.serialization.json.jsonPrimitive

fun EffectDescriptor.isChanceEffect(): Boolean {
	if (type != "multiplication") {
		return false
	}

	return try {
		val typeValue = config["type"]?.jsonPrimitive?.content
		typeValue == "chance"
	} catch (_: Exception) {
		false
	}
}
