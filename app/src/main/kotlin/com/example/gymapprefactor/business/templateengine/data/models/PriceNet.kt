package com.example.gymapprefactor.business.templateengine.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PriceNet(
	val type: String,
	val amount: Double
)
