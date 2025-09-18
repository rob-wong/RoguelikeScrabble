package com.example.gymapprefactor.business.models.exercise

interface Template {
    val id: String
    val exercise: Exercise
    val sets: Int
}

class DefaultTemplate(
    override val id: String,
    override val exercise: Exercise,
    override val sets: Int,
) : Template
