package com.cypherose.features.upgrade.presentation.models

import com.cypherose.business.models.Letter

data class UpgradeAnimationPayload(
	val originalLetter: Letter,
	val upgradedLetter: Letter,
)
