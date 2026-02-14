package com.example.gymapprefactor.features.upgrade.domain

import com.example.gymapprefactor.business.interfaces.Mapper
import javax.inject.Inject

interface UpgradeCostMapper : Mapper<Int, Int> {
	companion object {
		const val MAX_LETTER_LEVEL = 5
	}
}

class UpgradeCostMapperImpl @Inject constructor() : UpgradeCostMapper {
	override fun map(level: Int): Int {
		return level
	}
}
