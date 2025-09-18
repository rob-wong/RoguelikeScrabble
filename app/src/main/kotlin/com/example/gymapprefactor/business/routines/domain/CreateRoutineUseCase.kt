package com.example.gymapprefactor.business.routines.domain

import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import java.util.UUID
import javax.inject.Inject

class CreateRoutineUseCase @Inject constructor(
    private val saveRoutineUseCase: SaveRoutineUseCase,
) {
    suspend operator fun invoke(): Routine {
        val routine = createRoutine()
        saveRoutineUseCase(routine)
        return routine
    }

    private fun createRoutine(): Routine {
        return DefaultRoutine(
            id = UUID.randomUUID().toString(),
            templates = mutableListOf(),
            name = "New Routine",
        )
    }
}
