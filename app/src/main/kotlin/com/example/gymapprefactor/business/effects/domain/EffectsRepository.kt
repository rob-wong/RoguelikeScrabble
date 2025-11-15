package com.example.gymapprefactor.business.effects.domain

import kotlinx.coroutines.flow.Flow

interface EffectsRepository {
	fun getEffects(): Flow<Map<String, Int>>
}
