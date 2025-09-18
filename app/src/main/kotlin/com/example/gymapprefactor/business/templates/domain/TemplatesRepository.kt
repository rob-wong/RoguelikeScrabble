package com.example.gymapprefactor.business.templates.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.exercise.Template

interface TemplatesRepository : Repository {
    suspend fun getExerciseSetups(): List<Template>
    suspend fun saveExerciseSetup(template: Template)
}
