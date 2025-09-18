package com.example.gymapprefactor.features.routines.edit.presentation.state.events

import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class RoutineEventReducerImplTest {
    private val sut = RoutineEventReducerImpl()

    private val routine = mockk<Routine>()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = EditRoutineEventAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given SelectRoutine action, When update called, Then Correct State expected`() = runTest {
        // Given
        val action = EditRoutineEventAction.SelectRoutine(routine)

        // When
        sut.update(action)

        // Then
        val expectedState = EditRoutineEventState.SelectRoutine(routine)
        assertEquals(expectedState, sut.state.value)
    }

    @Test
    fun `Given CreateRoutine action, When update called, Then Correct State expected`() = runTest {
        // Given
        val action = EditRoutineEventAction.CreateRoutine

        // When
        sut.update(action)

        // Then
        val expectedState = EditRoutineEventState.CreateRoutine
        assertEquals(expectedState, sut.state.value)
    }

    @Test
    fun `Given RoutineUpdated action, When update called, Then Correct State expected`() = runTest {
        // Given
        val action = EditRoutineEventAction.RoutineUpdated(routine)

        // When
        sut.update(action)

        // Then
        val expectedState = EditRoutineEventState.RoutineUpdated(routine)
        assertEquals(expectedState, sut.state.value)
    }
}
