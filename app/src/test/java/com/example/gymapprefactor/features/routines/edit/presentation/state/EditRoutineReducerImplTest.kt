package com.example.gymapprefactor.features.routines.edit.presentation.state

import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension
import testextension.CoroutinesTestExtension.Companion.universalTopBar

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class EditRoutineReducerImplTest {
    private val rectangleButtonMapper = mockk<RectangleButtonMapper>()
    private val sut = EditRoutineReducerImpl(rectangleButtonMapper)

    private val nameInputField: TextInputFieldState = mockk()
    private val exerciseSetupList: List<RectangleButtonState> = mockk()
    private val onClickAddExercise: () -> Unit = mockk()
    private val onSave: () -> Unit = mockk()

    private val mockAddExerciseButton: RectangleButtonState = mockk()
    private val mockSaveButton: RectangleButtonState = mockk()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = EditRoutineAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given SetContent action, When update called, Then Content State expected`() = runTest {
        // Given
        val action = EditRoutineAction.SetContent(
            universalTopBar,
            nameInputField,
            exerciseSetupList,
            onClickAddExercise,
            onSave
        )

        every {
            rectangleButtonMapper.map(
                RectangleButtonMapper.Param(
                    onClick = onClickAddExercise,
                    text = "Add Exercise"
                )
            )
        } returns mockAddExerciseButton

        every {
            rectangleButtonMapper.map(
                RectangleButtonMapper.Param(
                    onClick = onSave,
                    text = "Save"
                )
            )
        } returns mockSaveButton

        // When
        sut.update(action)

        // Then
        val expectedState = EditRoutineState.Content(
            nameInputField,
            exerciseSetupList,
            mockAddExerciseButton,
            mockSaveButton,
            universalTopBar
        )
        assertEquals(expectedState, sut.state.value)

        verify(exactly = 1) {
            rectangleButtonMapper.map(
                RectangleButtonMapper.Param(
                    onClick = onSave,
                    text = "Save"
                )
            )
        }

        verify(exactly = 1) {
            rectangleButtonMapper.map(
                RectangleButtonMapper.Param(
                    onClick = onClickAddExercise,
                    text = "Add Exercise"
                )
            )
        }
    }
}
