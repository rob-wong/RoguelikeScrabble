package com.cypherose.business.templateengine.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductNet(
	val type: String,
	val amount: Int? = null,
	val decktype: String? = null
)
