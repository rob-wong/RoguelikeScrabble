package com.example.gymapprefactor.business.models.exercise

interface ExerciseStats {
    val completedSets: Int
    val completedReps: Int
    val completedWorkouts: Int
    val startingReps: List<Int>
    val startingWeight: List<Int>
    val bestReps: List<Int>
    val bestWeight: List<Int>
    val currentReps: List<Int>
    val currentWeight: List<Int>
    val attempted1RM: Int
    val best1RM: Int
}

@Suppress("LongParameterList")
class DefaultExerciseStats(
    override val completedSets: Int = 0,
    override val completedReps: Int = 0,
    override val completedWorkouts: Int = 0,
    override val startingReps: List<Int> = listOf(),
    override val startingWeight: List<Int> = listOf(),
    override val bestReps: List<Int> = listOf(),
    override val bestWeight: List<Int> = listOf(),
    override val currentReps: List<Int> = listOf(),
    override val currentWeight: List<Int> = listOf(),
    override val attempted1RM: Int = 0,
    override val best1RM: Int = 0,
) : ExerciseStats
