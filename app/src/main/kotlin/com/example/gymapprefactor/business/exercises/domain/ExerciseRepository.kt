package com.example.gymapprefactor.business.exercises.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.exercise.Exercise

interface ExerciseRepository : Repository {
    suspend fun getExercises(): List<Exercise>
}
