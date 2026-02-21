package com.cypherose.business.templateengine.domain

import com.cypherose.business.interfaces.Repository

interface TemplateRepository : Repository {
	suspend fun fetchTemplateData(path: String): String
}
