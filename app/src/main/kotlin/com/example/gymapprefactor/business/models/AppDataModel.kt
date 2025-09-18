package com.example.gymapprefactor.business.models

import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import javax.inject.Singleton

@Singleton
object AppDataModel {
    // TODO: add a backend and start integration testing ;p
    lateinit var exerciseList: MutableList<Exercise>
    lateinit var templateList: MutableList<Template>
    lateinit var routineList: MutableList<Routine>

    fun initData() {
        fetchExercises()
        fetchExerciseSetups()
        fetchRoutines()
    }

    private fun fetchExercises() {
        exerciseList = TemporaryDataFill.exerciseList
    }

    private fun fetchExerciseSetups() {
        templateList = TemporaryDataFill.templateList
    }

    private fun fetchRoutines() {
        routineList = TemporaryDataFill.routineList
    }
}
