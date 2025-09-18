package com.example.gymapprefactor.business.exercises.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.exercise.Exercise

class GetExercisesUseCase(override val repository: ExerciseRepository) : UseCase {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getExercises()
    }
}
