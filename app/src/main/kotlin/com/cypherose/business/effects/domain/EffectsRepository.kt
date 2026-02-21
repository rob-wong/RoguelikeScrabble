package com.cypherose.business.effects.domain

import com.cypherose.business.effects.templating.domain.EffectDescriptor
import kotlinx.coroutines.flow.Flow

interface EffectsRepository {
	fun getEffectDescriptors(): Flow<Map<String, EffectDescriptor>>
}
