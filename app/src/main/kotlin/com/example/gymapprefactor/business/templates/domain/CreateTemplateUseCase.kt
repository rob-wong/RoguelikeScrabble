package com.example.gymapprefactor.business.templates.domain

import com.example.gymapprefactor.business.models.exercise.DefaultTemplate
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import java.util.UUID
import javax.inject.Inject

class CreateTemplateUseCase @Inject constructor(
    private val saveTemplateUseCase: SaveTemplateUseCase,
) {
    suspend operator fun invoke(
        exercise: Exercise,
        sets: Int,
    ): Template {
        val exerciseSetup = createExerciseSetup(exercise, sets)
        saveTemplateUseCase(exerciseSetup)
        return exerciseSetup
    }

    private fun createExerciseSetup(
        exercise: Exercise,
        sets: Int,
    ): Template {
        return DefaultTemplate(
            id = UUID.randomUUID().toString(),
            exercise = exercise,
            sets = sets
        )
    }
}
