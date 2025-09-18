package com.example.gymapprefactor.features.homeScreen.presentation.state

import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState
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
class HomeScreenReducerImplTest {
    private val sut = HomeScreenReducerImpl()

    private val onExercises: RectangleButtonState = mockk()
    private val onRoutines: RectangleButtonState = mockk()
    private val onViewStats: RectangleButtonState = mockk()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = HomeScreenAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given SetContent action, When update called, Then Content State expected`() = runTest {
        // Given
        val action = HomeScreenAction.SetContent(
            onExercises,
            onRoutines,
            onViewStats,
            universalTopBar
        )

        // When
        sut.update(action)

        // Then
        val expectedState = HomeScreenState.Content(
            onExercises,
            onRoutines,
            onViewStats,
            universalTopBar
        )
        assertEquals(expectedState, sut.state.value)
    }
}
