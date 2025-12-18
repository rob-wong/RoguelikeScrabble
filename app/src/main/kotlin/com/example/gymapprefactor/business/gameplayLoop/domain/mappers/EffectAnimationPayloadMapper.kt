package com.example.gymapprefactor.business.gameplayLoop.domain.mappers

import com.example.gymapprefactor.business.interfaces.Mapper
import com.example.gymapprefactor.features.game.presentation.models.animation.EffectAnimationPayload
import javax.inject.Inject

interface EffectAnimationPayloadMapper : Mapper<EffectAnimationPayloadMapper.Param, List<EffectAnimationPayload>> {
	data class Param(
		val effectModifications: List<EffectScoreModification>,
		val rawScore: Int
	)
}

class EffectAnimationPayloadMapperImpl @Inject constructor() : EffectAnimationPayloadMapper {
	override fun map(param: EffectAnimationPayloadMapper.Param): List<EffectAnimationPayload> {
		var cumulativeScore = param.rawScore
		return param.effectModifications.map { modification ->
			if (modification.glyphAmount > 0) {
				EffectAnimationPayload.Glyph(
					effectId = modification.effectId,
					effectLabel = modification.effectLabel,
					glyphAmount = modification.glyphAmount,
					orderIndex = modification.orderIndex
				)
			} else {
				cumulativeScore += modification.scoreDelta
				EffectAnimationPayload.Score(
					effectId = modification.effectId,
					effectLabel = modification.effectLabel,
					scoreDelta = modification.scoreDelta,
					orderIndex = modification.orderIndex,
					cumulativeScore = cumulativeScore,
					multiplier = modification.multiplier
				)
			}
		}
	}
}
