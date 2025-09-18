package com.example.gymapprefactor.business.routines.domain

import com.example.gymapprefactor.business.interfaces.Repository
import com.example.gymapprefactor.business.models.Routine

interface RoutinesRepository : Repository {
    suspend fun getRoutines(): List<Routine>
    suspend fun saveRoutine(routine: Routine): Routine
}
