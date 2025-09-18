package com.example.gymapprefactor.business.routines.domain

import com.example.gymapprefactor.business.models.DefaultRoutine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetRoutinesUseCaseTest {
    private val mockRoutine = mockk<DefaultRoutine>()
    private val repository = mockk<RoutinesRepository> {
        coEvery { getRoutines() } returns listOf(mockRoutine)
    }

    private val sut = GetRoutinesUseCase(repository)

    @Test
    fun `When invoke, Then repository call expected`() = runTest {
        // When
        sut.invoke()

        // Then
        coVerify(exactly = 1) { repository.getRoutines() }
    }
}
