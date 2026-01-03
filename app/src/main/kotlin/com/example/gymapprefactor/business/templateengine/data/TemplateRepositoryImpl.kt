package com.example.gymapprefactor.business.templateengine.data

import com.example.gymapprefactor.business.templateengine.domain.TemplateRepository

class TemplateRepositoryImpl(
	override val dataSource: TemplateDataSource
) : TemplateRepository {
	override suspend fun fetchTemplateData(): String {
		return dataSource.fetchTemplateData()
	}
}
