package com.cypherose.business.effects.domain

import com.cypherose.business.effects.templating.domain.EffectDescriptor
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetEffectDescriptorsUseCase @Inject constructor(
	private val effectsRepository: EffectsRepository
) {
	suspend operator fun invoke(): Map<String, EffectDescriptor> {
		return effectsRepository.getEffectDescriptors().first()
	}
}

