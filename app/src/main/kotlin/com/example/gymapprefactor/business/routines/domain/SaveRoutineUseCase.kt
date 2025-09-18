package com.example.gymapprefactor.business.routines.domain

import com.example.gymapprefactor.business.models.Routine

class SaveRoutineUseCase(val repository: RoutinesRepository) {
    // ERRORS
    // need an id
    // name too long (pick arbitrary number)
    // blank name
    // invalid characters

    suspend operator fun invoke(routine: Routine): Routine {
        //error check
        return repository.saveRoutine(routine)
    }
}
