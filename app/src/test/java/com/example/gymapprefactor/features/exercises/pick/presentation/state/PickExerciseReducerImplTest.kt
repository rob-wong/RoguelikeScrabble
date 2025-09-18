package com.example.gymapprefactor.features.exercises.pick.presentation.state

import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseAction
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension
import testextension.CoroutinesTestExtension.Companion.universalTopBar

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PickExerciseReducerImplTest {
    private val sut = PickExerciseReducerImpl()

    private val exerciseList: List<RectangleButtonState> = mockk()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = PickExerciseAction.None
        val originalState = sut.state.value

        // When
        val result = sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
        assertEquals(result, Unit)
    }

    @Test
    fun `Given SetContent action, When update called, Then content state expected`() = runTest {
        // Given
        val action = PickExerciseAction.SetContent(exerciseList, universalTopBar)

        // When
        sut.update(action)

        // Then
        val expectedState = PickExerciseState.Content(exerciseList, universalTopBar)
        assertEquals(expectedState, sut.state.value)
    }
}
