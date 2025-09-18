package com.example.gymapprefactor.business.templates.domain

import com.example.gymapprefactor.business.models.exercise.Template

class SaveTemplateUseCase(val repository: TemplatesRepository) {

    suspend operator fun invoke(template: Template) {
        repository.saveExerciseSetup(template)
    }
}
