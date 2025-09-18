package com.example.gymapprefactor.business.routines.domain

import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.routines.data.RoutinesRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SaveRoutineUseCaseTest {
    private val mockRoutine = mockk<DefaultRoutine>()

    private val mockRepository = mockk<RoutinesRepositoryImpl> {
        coEvery { saveRoutine(mockRoutine) } returns mockRoutine
    }

    private val sut = SaveRoutineUseCase(mockRepository)

    @Test
    fun `When invoke, Then repository call expected`() = runTest {
        // When
        sut.invoke(mockRoutine)

        // Then
        coVerify(exactly = 1) { mockRepository.saveRoutine(mockRoutine) }
    }
}
