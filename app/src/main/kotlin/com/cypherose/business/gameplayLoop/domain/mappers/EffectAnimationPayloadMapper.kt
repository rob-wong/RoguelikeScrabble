package com.cypherose.business.gameplayLoop.domain.mappers

import com.cypherose.business.interfaces.Mapper
import com.cypherose.features.game.presentation.models.animation.EffectAnimationPayload
import javax.inject.Inject

interface EffectAnimationPayloadMapper : Mapper<EffectAnimationPayloadMapper.Param, List<EffectAnimationPayload>> {
	data class Param(
		val effectModifications: List<EffectScoreModification>,
		val rawScore: Int
	)
}

class EffectAnimationPayloadMapperImpl @Inject constructor() : EffectAnimationPayloadMapper {
	override fun map(param: EffectAnimationPayloadMapper.Param): List<EffectAnimationPayload> {
		android.util.Log.d(
			"EffectAnimationPayloadMapper",
			"map: Starting with ${param.effectModifications.size} modifications, " +
				"rawScore=${param.rawScore}"
		)
		var cumulativeScore = param.rawScore
		return param.effectModifications.mapIndexed { index, modification ->
			android.util.Log.d(
				"EffectAnimationPayloadMapper",
				"map: Processing modification[$index]: id=${modification.effectId}, " +
					"label=${modification.effectLabel}, glyphAmount=${modification.glyphAmount}, " +
					"multiplier=${modification.multiplier}, isChance=${modification.isChanceMultiplier}"
			)
			if (modification.glyphAmount > 0) {
				val payload = EffectAnimationPayload.Glyph(
					effectId = modification.effectId,
					effectLabel = modification.effectLabel,
					glyphAmount = modification.glyphAmount,
					orderIndex = modification.orderIndex
				)
				android.util.Log.d("EffectAnimationPayloadMapper", "map: Created Glyph payload[$index]: $payload")
				payload
			} else {
				cumulativeScore += modification.scoreDelta
				val payload = EffectAnimationPayload.Score(
					effectId = modification.effectId,
					effectLabel = modification.effectLabel,
					scoreDelta = modification.scoreDelta,
					orderIndex = modification.orderIndex,
					cumulativeScore = cumulativeScore,
					multiplier = modification.multiplier,
					isChanceMultiplier = modification.isChanceMultiplier,
				)
				android.util.Log.d(
					"EffectAnimationPayloadMapper",
					"map: Created Score payload[$index]: id=${payload.effectId}, " +
						"multiplier=${payload.multiplier}, isChance=${payload.isChanceMultiplier}, " +
						"cumulativeScore=$cumulativeScore"
				)
				payload
			}
		}
	}
}
