package com.example.gymapprefactor.features.upgrade.presentation.models

import com.example.gymapprefactor.business.models.Letter

data class UpgradeAnimationPayload(
	val originalLetter: Letter,
	val upgradedLetter: Letter,
)
