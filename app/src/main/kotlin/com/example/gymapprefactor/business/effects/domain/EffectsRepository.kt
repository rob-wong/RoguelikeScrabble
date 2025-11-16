package com.example.gymapprefactor.business.effects.domain

import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import kotlinx.coroutines.flow.Flow

interface EffectsRepository {
	fun getEffects(): Flow<Map<String, Int>>
	fun getEffectDescriptors(): Flow<Map<String, EffectDescriptor>>
}
