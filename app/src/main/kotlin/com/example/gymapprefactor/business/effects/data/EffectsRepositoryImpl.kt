package com.example.gymapprefactor.business.effects.data

import com.example.gymapprefactor.business.effects.domain.EffectsRepository
import com.example.gymapprefactor.business.effects.templating.domain.EffectDescriptor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EffectsRepositoryImpl @Inject constructor(
	private val dataSource: EffectsDataSource
) : EffectsRepository {
	override fun getEffectDescriptors(): Flow<Map<String, EffectDescriptor>> {
		return dataSource.getEffectDescriptors()
	}
}
