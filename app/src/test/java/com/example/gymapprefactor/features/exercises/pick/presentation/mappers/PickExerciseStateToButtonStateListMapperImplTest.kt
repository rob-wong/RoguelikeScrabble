package com.example.gymapprefactor.features.exercises.pick.presentation.mappers

import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PickExerciseStateToButtonStateListMapperImplTest {
    private val sut = PickTemplateToButtonListMapperImpl()

    private val exerciseName = "exerciseName"
    private val mockExercise = mockk<Exercise> {
        every { name } returns exerciseName
        every { id } returns "id"
    }
    private val exerciseName2 = "exerciseName2"
    private val mockExercise2 = mockk<Exercise> {
        every { name } returns exerciseName2
        every { id } returns "id2"
    }

    private val mockTemplate: Template = mockk {
        every { exercise } returns mockExercise
    }
    private val filledExerciseSetups = listOf(mockTemplate)
    private val mockFullRoutine = mockk<Routine> {
        every { templates } returns filledExerciseSetups
    }

    private val mockTemplates: List<Template> = listOf()
    private val mockEmptyRoutine = mockk<Routine> {
        every { templates } returns mockTemplates
    }

    private val routinesEndPage = mockk<ExerciseEventState.PickEndPage.Routines> {
        every { routine } returns mockEmptyRoutine
    }
    private val statsEndPage = mockk<ExerciseEventState.PickEndPage.Stats>()

    private val mockExerciseList: List<Exercise> = listOf(mockExercise, mockExercise2)
    private val mockOnClick: (Exercise) -> Unit = spyk()

    @Test
    fun `Given Routines EndPage, When map, Then correct ButtonStates expected`() = runTest {
        // Given
        val param = getParam(ExerciseEventState.PickExercise(routinesEndPage))

        // When
        val result = sut.map(param)

        // Then
        assertTrue(result[0] is RectangleButtonState.Content)
        val resultButton = result[0] as RectangleButtonState.Content

        assertTrue(resultButton.text == exerciseName)

        resultButton.onClick()
        verify(exactly = 1) { mockOnClick(mockExercise) }
    }

    @Test
    fun `Given Routines EndPage and routine has exercises, When map, Then those excluded expected`() = runTest {
        // Given
        every { routinesEndPage.routine } returns mockFullRoutine
        val param = getParam(ExerciseEventState.PickExercise(routinesEndPage))

        // When
        val result = sut.map(param)

        // Then
        assertTrue(result[0] is RectangleButtonState.Content)
        val resultButton = result[0] as RectangleButtonState.Content

        assertTrue(resultButton.text == exerciseName2)

        resultButton.onClick()
        verify(exactly = 1) { mockOnClick(mockExercise2) }
    }

    @Test
    fun `GIven Stats EndAPage, When map, Then correct buttons expected`() = runTest {
        // Given
        val param = getParam(ExerciseEventState.PickExercise(statsEndPage))

        // When
        val result = sut.map(param)

        // Then
        assertTrue(result[0] is RectangleButtonState.Content)
        val resultButton = result[0] as RectangleButtonState.Content

        assertTrue(resultButton.text == exerciseName)
        resultButton.onClick()
        verify(exactly = 1) { mockOnClick(mockExercise) }
    }

    private fun getParam(
        state: ExerciseEventState.PickExercise,
        allExercisesInData: List<Exercise> = mockExerciseList,
        onClick: (Exercise) -> Unit = mockOnClick
    ): PickTemplateToButtonListMapper.Param {
        return PickTemplateToButtonListMapper.Param(
            state = state,
            allExercisesInData = allExercisesInData,
            onClick = onClick
        )
    }
}
