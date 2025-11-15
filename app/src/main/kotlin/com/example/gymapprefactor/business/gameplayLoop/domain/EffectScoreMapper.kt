package com.example.gymapprefactor.business.gameplayLoop.domain

import com.example.gymapprefactor.business.effects.domain.EffectsRepository
import com.example.gymapprefactor.business.models.Effect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class EffectScoreModification(
	val effectId: String,
	val effectLabel: String,
	val scoreDelta: Int,
	val orderIndex: Int
)

interface EffectScoreMapper {
	data class Param(
		val effects: List<Effect>,
		val rawScore: Int
	)
	
	suspend fun map(param: Param): List<EffectScoreModification>
}

class EffectScoreMapperImpl @Inject constructor(
	private val effectsRepository: EffectsRepository
) : EffectScoreMapper {

	override suspend fun map(param: EffectScoreMapper.Param): List<EffectScoreModification> {
		val effectMap = effectsRepository.getEffects().first()
		
		return param.effects.mapIndexed { index, effect ->
			val scoreDelta = effectMap[effect.label] ?: effect.label.length
			EffectScoreModification(
				effectId = effect.id,
				effectLabel = effect.label,
				scoreDelta = scoreDelta,
				orderIndex = index
			)
		}
	}
}
