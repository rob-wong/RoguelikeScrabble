package com.example.gymapprefactor.features.exercises.setup.presentation.state

import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseAction
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import testextension.CoroutinesTestExtension.Companion.universalTopBar

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SetupExerciseReducerImplTest {

    private val sut = SetupExerciseReducerImpl()

    private val exercise: Exercise = mockk()
    private val onSave: RectangleButtonState = mockk()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = SetupExerciseAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given SetContent action, When update called, Then Content state expected`() = runTest {
        // Given
        val action = SetupExerciseAction.SetContent(exercise, onSave, universalTopBar)

        // When
        sut.update(action)

        // Then
        val expectedState = SetupExerciseState.Content(exercise, onSave, universalTopBar)
        assertEquals(expectedState, sut.state.value)
    }
}
