package com.cypherose.business.effects.data

import com.cypherose.business.effects.domain.EffectsRepository
import com.cypherose.business.effects.templating.domain.EffectDescriptor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EffectsRepositoryImpl @Inject constructor(
	private val dataSource: EffectsDataSource
) : EffectsRepository {
	override fun getEffectDescriptors(): Flow<Map<String, EffectDescriptor>> {
		return dataSource.getEffectDescriptors()
	}
}
