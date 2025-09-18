package com.example.gymapprefactor.business.templates.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.exercise.Template

class GetTemplatesUseCase(override val repository: TemplatesRepository) : UseCase {
    suspend operator fun invoke(): List<Template> {
        return repository.getExerciseSetups()
    }
}
