package com.example.gymapprefactor.business.templateengine.domain

import com.example.gymapprefactor.business.interfaces.Repository

interface TemplateRepository : Repository {
	suspend fun fetchTemplateData(path: String): String
}
