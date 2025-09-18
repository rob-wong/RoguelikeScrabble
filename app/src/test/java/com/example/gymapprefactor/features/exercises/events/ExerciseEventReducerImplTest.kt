package com.example.gymapprefactor.features.exercises.events

import com.example.gymapprefactor.business.models.exercise.Exercise
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ExerciseEventReducerImplTest {
    private val sut = ExerciseEventReducerImpl()

    private val pickEndPage: ExerciseEventState.PickEndPage = mockk()
    private val setupEndPage: ExerciseEventState.SetupEndPage = mockk()
    private val mockExercise: Exercise = mockk()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = ExerciseEventAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given PickExercise action, When update called, Then PickExercise state expected`() = runTest {
        // Given
        val action = ExerciseEventAction.PickExercise(
            endPage = pickEndPage
        )

        // When
        sut.update(action)

        // Then
        val expectedState = ExerciseEventState.PickExercise(pickEndPage)
        assertEquals(expectedState, sut.state.value)
    }

    @Test
    fun `Given SetupExercise action, When update called, Then SetupExercise state expected`() = runTest {
        // Given
        val action = ExerciseEventAction.SetupExercise(
            mockExercise,
            setupEndPage
        )

        // When
        sut.update(action)

        // THen
        val expectedState = ExerciseEventState.SetupExercise(mockExercise, setupEndPage)
        assertEquals(expectedState, sut.state.value)
    }
}
