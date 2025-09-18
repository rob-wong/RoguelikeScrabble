package com.example.gymapprefactor.business.models.exercise

import com.example.gymapprefactor.business.models.Equipment
import com.example.gymapprefactor.business.models.MuscleGroup

interface Exercise {
    val id: String
    val name: String
    val muscleGroups: List<MuscleGroup>
    val equipment: Equipment
    val exerciseStats: ExerciseStats
}

class DefaultExercise(
    override val id: String,
    override val name: String,
    override val muscleGroups: List<MuscleGroup>,
    override val equipment: Equipment,
    override val exerciseStats: ExerciseStats
) : Exercise
