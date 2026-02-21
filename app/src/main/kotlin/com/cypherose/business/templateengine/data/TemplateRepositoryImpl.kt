package com.cypherose.business.templateengine.data

import com.cypherose.business.templateengine.domain.TemplateRepository

class TemplateRepositoryImpl(
	override val dataSource: TemplateDataSource
) : TemplateRepository {
	override suspend fun fetchTemplateData(path: String): String {
		return dataSource.fetchTemplateData(path)
	}
}
