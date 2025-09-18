package com.example.gymapprefactor.business.models

import com.example.gymapprefactor.business.models.exercise.DefaultExercise
import com.example.gymapprefactor.business.models.exercise.DefaultExerciseStats
import com.example.gymapprefactor.business.models.MuscleGroup.*
import com.example.gymapprefactor.business.models.exercise.DefaultTemplate
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import javax.inject.Singleton

@Singleton
object TemporaryDataFill {
    val exerciseList = mutableListOf<Exercise>(
        DefaultExercise(
            name = "Barbell Bench Press",
            muscleGroups = listOf(
                Chest(
                    listOf(
                        ChestSection.Middle,
                        ChestSection.Upper,
                        ChestSection.Lower,
                    )
                ),
                Shoulders(listOf(ShoulderSection.Front)),
                Triceps(
                    listOf(
                        TricepsSection.LateralHead,
                        TricepsSection.MedialHead,
                        TricepsSection.LongHead,
                    )
                )
            ),
            equipment = Equipment.Barbell,
            exerciseStats = DefaultExerciseStats(),
            id = "e-id-1",
        ),
        DefaultExercise(
            name = "Trap Pull Ups",
            muscleGroups = listOf(
                Traps(
                    listOf(
                        TrapsSection.Upper,
                    )
                )
            ),
            equipment = Equipment.BodyWeight,
            exerciseStats = DefaultExerciseStats(),
            id = "e-id-1",
        ),
    )

    val templateList = mutableListOf<Template>(
        DefaultTemplate(
            exercise = exerciseList[1],
            sets = 4,
            id = "s-id-1",
        ),
        DefaultTemplate(
            exercise = exerciseList[0],
            sets = 4,
            id = "s-id-2",
        ),
    )

    val routineList = mutableListOf<Routine>(
        DefaultRoutine(
            templates = listOf(
                templateList[0],
                templateList[1],
            ),
            name = "Routine 1",
            id = "r-id-1",
        ),
        DefaultRoutine(
            templates = listOf(
                templateList[0],
            ),
            name = "Routine 2",
            id = "r-id-2",
        ),
    )
}
