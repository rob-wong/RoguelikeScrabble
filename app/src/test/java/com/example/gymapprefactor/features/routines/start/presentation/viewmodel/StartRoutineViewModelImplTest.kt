package com.example.gymapprefactor.features.routines.start.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducerImpl
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineAction
import com.example.gymapprefactor.features.routines.start.presentation.models.StartRoutineState
import com.example.gymapprefactor.features.routines.start.presentation.state.StartRoutineReducerImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class StartRoutineViewModelImplTest {
    private val mockState = mockk<MutableStateFlow<StartRoutineState>> {
        every { value } returns StartRoutineState.None
    }

    private val startRoutineReducer = mockk<StartRoutineReducerImpl> {
        every { state } returns mockState
    }

    private val navigationReducer = mockk<NavigationReducerImpl> {
        every { state.value } returns NavigationState.None
    }

    private val testScope = TestScope(CoroutinesTestExtension.testDispatcher)

    @Test
    fun `Given sut, When init, Then SetContentAction Expected`() = runTest {
        // When
        val action = initViewModel()

        // Then
        assertTrue(action.topBarState is TopBarState.Content)
        assertEquals("Start Routine", (action.topBarState as TopBarState.Content).title)
    }

    @Test
    fun `Given init, When onBack, Then back navigation expected`() = runTest {
        // Given
        coEvery { navigationReducer.update(NavigationAction.GoBack) } just Runs
        val action = initViewModel()

        // When
        (action.topBarState as TopBarState.Content).onBack()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { navigationReducer.update(NavigationAction.GoBack) }
    }

    private fun initViewModel(): StartRoutineAction.SetContent {
        // Given
        val action = slot<StartRoutineAction.SetContent>()
        coEvery { startRoutineReducer.update(capture(action)) } returns Unit

        // When
        getSut()
        testScope.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { startRoutineReducer.update(action.captured) }
        assertTrue(action.isCaptured)
        return action.captured
    }

    private fun getSut(): StartRoutineViewModelImpl {
        return StartRoutineViewModelImpl(
            startRoutineReducer = startRoutineReducer,
            navigationReducer = navigationReducer,
            dispatcherProvider = TestDispatcherProvider(CoroutinesTestExtension.testDispatcher)
        )
    }
}
