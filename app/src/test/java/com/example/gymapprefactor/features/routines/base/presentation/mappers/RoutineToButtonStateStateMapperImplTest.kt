package com.example.gymapprefactor.features.routines.base.presentation.mappers

import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class RoutineToButtonStateStateMapperImplTest {
    private val sut = RoutineToButtonStateMapperImpl()
    private val testName = "name"

    private val defaultRoutine = mockk<DefaultRoutine> {
        every { name } returns testName
        every { templates } returns listOf()
    }
    private val elseRoutine = mockk<Routine>()

    private val editRoutineEventCallback: (Routine) -> Unit = spyk()
    private val navigationCallback: () -> Unit = spyk()

    @Test
    fun `Given param is DefaultRoutine, When mapped, Then button Content state expected`() {
        // Given
        val param = getParam(
            defaultRoutine,
            editRoutineEventCallback,
            navigationCallback
        )

        // When
        val result = sut.map(param)

        // Then
        assert(result is RectangleButtonState.Content)
        assertEquals(testName, (result as RectangleButtonState.Content).text)

        result.onClick()
        coVerify(exactly = 1) {
            navigationCallback()
            editRoutineEventCallback(defaultRoutine)
        }
    }

    @Test
    fun `Given param not DefaultRoutine, When mapped, Then None state expected`() {
        // Given
        val param = getParam(
            elseRoutine,
            editRoutineEventCallback,
            navigationCallback
        )

        // When
        val result = sut.map(param)

        // Then
        assertTrue(result is RectangleButtonState.None)
    }

    private fun getParam(
        routine: Routine,
        editRoutineEventCallback: (Routine) -> Unit,
        navigationCallback: () -> Unit
    ): RoutineToButtonStateMapper.Param {
        return RoutineToButtonStateMapper.Param(
            routine = routine,
            navigationCallback = navigationCallback,
            editRoutineEventCallback = editRoutineEventCallback
        )
    }
}
