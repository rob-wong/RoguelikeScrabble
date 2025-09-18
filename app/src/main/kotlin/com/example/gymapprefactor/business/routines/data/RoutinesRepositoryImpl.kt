package com.example.gymapprefactor.business.routines.data

import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.routines.domain.RoutinesRepository

class RoutinesRepositoryImpl(override val dataSource: RoutineDataSource) : RoutinesRepository {
    override suspend fun getRoutines(): List<Routine> {
        return dataSource.fetchRoutines()
    }

    override suspend fun saveRoutine(routine: Routine): Routine {
        return dataSource.saveRoutine(routine)
    }
}

class RoutineDataSource : DataSource {

    fun fetchRoutines(): List<Routine> {
        return AppDataModel.routineList
    }

    fun saveRoutine(routineBeingSaved: Routine): Routine {
        AppDataModel.routineList.forEachIndexed { index, routine ->
            if (routine.id == routineBeingSaved.id) {
                AppDataModel.routineList[index] = routineBeingSaved
                return routineBeingSaved
            }
        }
        AppDataModel.routineList.add(routineBeingSaved)
        return routineBeingSaved
    }
}
