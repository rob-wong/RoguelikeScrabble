package com.example.gymapprefactor.features.routines.base.presentation.mappers

import com.example.gymapprefactor.business.models.exercise.DefaultTemplate
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import com.example.gymapprefactor.common.components.presentation.FoldingButtonState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TemplateToButtonStateStateMapperImplTest {
    private val sut = TemplateToButtonStateMapperImpl()

    private val elseSetup = mockk<Template>()
    private val removeExerciseCallback: (String) -> Unit = spyk()
    private val editExerciseCallback: () -> Unit = spyk()

    private val exerciseName = "exerciseName"
    private val mockExercise = mockk<Exercise> {
        every { name } returns exerciseName
    }

    private val mockId = "id"
    private val defaultExerciseSetup = mockk<DefaultTemplate> {
        every { exercise } returns mockExercise
        every { id } returns mockId
    }

    @Test
    fun `Given DefaultExerciseSetup param, When mapped, Then Folding button Content State expected`() = runTest {
        // Given
        val param = getParam(
            defaultExerciseSetup,
            removeExerciseCallback = removeExerciseCallback,
            editExerciseCallback = editExerciseCallback
        )

        // When
        val result = sut.map(param) as FoldingButtonState

        // Then
        assert(result is FoldingButtonState.Content)
        assertEquals(exerciseName, (result as FoldingButtonState.Content).text)

        result.buttons[1].onClick()
        coVerify(exactly = 1) {
            editExerciseCallback()
        }

        result.buttons[0].onClick()
        coVerify(exactly = 1) {
            removeExerciseCallback(mockId)
        }
    }

    @Test
    fun `Given param not DefaultExerciseSetup, When mapped, Then None state expected`() = runTest {
        // Given
        val param = getParam(
            elseSetup,
            removeExerciseCallback = removeExerciseCallback,
            editExerciseCallback = editExerciseCallback
        )

        // When
        val result = sut.map(param)

        // Then
        assertTrue(result is FoldingButtonState.None)
    }

    private fun getParam(
        template: Template,
        removeExerciseCallback: (String) -> Unit,
        editExerciseCallback: () -> Unit
    ): TemplateToButtonStateMapper.Param {
        return TemplateToButtonStateMapper.Param(
            template = template,
            removeExerciseCallback = removeExerciseCallback,
            editExerciseCallback = editExerciseCallback
        )
    }
}
