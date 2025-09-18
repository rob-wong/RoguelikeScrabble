package com.example.gymapprefactor.features.routines.start.presentation.state

import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineAction
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension
import kotlin.test.assertEquals

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class StartRoutineReducerImplTest {
    private val sut = StartRoutineReducerImpl()

    private val mockTopBar = mockk<TopBarState>()

    @Test
    fun `Given None action, When update, Then None state expected`() = runTest {
        // Given
        val action = StartRoutineAction.None

        // When
        sut.update(action)

        // Then
        val expectedState = StartRoutineState.None
        assertEquals(expectedState, sut.state.value)
    }

    @Test
    fun `Given SetContent action, When update, Then Content state expected`() = runTest {
        // Given
        val action = StartRoutineAction.SetContent(
            topBarState = mockTopBar
        )

        // When
        sut.update(action)

        // Then
        val expectedState = StartRoutineState.Content(
            topBarState = mockTopBar
        )
        assertEquals(expectedState, sut.state.value)
    }
}
