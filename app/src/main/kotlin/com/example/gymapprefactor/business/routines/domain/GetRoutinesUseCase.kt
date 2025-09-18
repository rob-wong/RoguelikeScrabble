package com.example.gymapprefactor.business.routines.domain

import com.example.gymapprefactor.business.interfaces.UseCase
import com.example.gymapprefactor.business.models.Routine

class GetRoutinesUseCase(override val repository: RoutinesRepository) : UseCase {
    suspend operator fun invoke(): List<Routine> {
        return repository.getRoutines()
    }
}
