package com.example.gymapprefactor.business.effects.data

import com.example.gymapprefactor.business.effects.domain.EffectsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EffectsRepositoryImpl @Inject constructor(
	private val dataSource: EffectsDataSource
) : EffectsRepository {
	override fun getEffects(): Flow<Map<String, Int>> {
		return dataSource.getEffects()
	}
}
