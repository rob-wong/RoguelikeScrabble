package com.example.gymapprefactor.business.exercises.data

import com.example.gymapprefactor.business.exercises.domain.ExerciseRepository
import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.exercise.Exercise

class ExerciseRepositoryImpl(
    override val dataSource: ExerciseDataSource
) : ExerciseRepository {
    override suspend fun getExercises(): List<Exercise> {
        return dataSource.fetchExercises()
    }
}

class ExerciseDataSource : DataSource {
    fun fetchExercises(): List<Exercise> {
        return AppDataModel.exerciseList
    }
}
