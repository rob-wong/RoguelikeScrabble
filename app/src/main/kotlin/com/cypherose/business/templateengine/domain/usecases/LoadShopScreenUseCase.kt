package com.cypherose.business.templateengine.domain.usecases

import com.cypherose.business.interfaces.UseCase
import com.cypherose.business.templateengine.domain.TemplateEngine
import com.cypherose.business.templateengine.domain.TemplateInstance
import com.cypherose.business.templateengine.domain.TemplateRepository
import javax.inject.Inject

class LoadShopScreenUseCase @Inject constructor(
	override val repository: TemplateRepository,
	private val templateEngine: TemplateEngine
) : UseCase {
	suspend operator fun invoke(path: String): Result<List<TemplateInstance>> {
		return try {
			val jsonString = repository.fetchTemplateData(path)
			val instances = templateEngine.parseAndCreateInstances(jsonString)
			Result.success(instances)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
