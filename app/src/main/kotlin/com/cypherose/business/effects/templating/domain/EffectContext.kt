package com.cypherose.business.effects.templating.domain

data class EffectContext(
	val seed: Long,
	val level: Int,
	val round: Int,
	val effectIndex: Int
) {
	fun combineSeedForEffect(): Long {
		return (seed xor
				(level.toLong() shl 32) xor
				(round.toLong() shl 16) xor
				(effectIndex.toLong() shl 8))
	}
}
