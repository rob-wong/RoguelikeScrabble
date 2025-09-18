package com.example.gymapprefactor.features.routines.base.presentation.state

import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenAction
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension
import testextension.CoroutinesTestExtension.Companion.universalTopBar

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BaseRoutineScreenReducerImplTest {

    private val sut = BaseRoutineScreenReducerImpl()
    private val buttonList: List<RectangleButtonState> = mockk()
    private val createButton: RectangleButtonState = mockk()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = BaseRoutineScreenAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given SetContent action, When update called, Then Content state expected`() = runTest {
        // Given
        val action = BaseRoutineScreenAction.SetContent(
            buttonList,
            createButton,
            universalTopBar
        )

        // When
        sut.update(action)

        // Then
        val expectedState = BaseRoutineScreenState.Content(
            buttonList,
            createButton,
            universalTopBar
        )
        assertEquals(expectedState, sut.state.value)
    }
}
