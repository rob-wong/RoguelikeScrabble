package com.example.gymapprefactor.business.templateengine.domain.usecases

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.templateengine.domain.TemplateEngine
import com.example.gymapprefactor.business.templateengine.domain.TemplateInstance
import com.example.gymapprefactor.business.templateengine.domain.TemplateRepository
import javax.inject.Inject

class LoadShopScreenUseCase @Inject constructor(
	override val repository: TemplateRepository,
	private val templateEngine: TemplateEngine
) : UseCase {
	suspend operator fun invoke(): Result<List<TemplateInstance>> {
		return try {
			val jsonString = repository.fetchTemplateData()
			val instances = templateEngine.parseAndCreateInstances(jsonString)
			Result.success(instances)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
