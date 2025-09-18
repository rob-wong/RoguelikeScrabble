package com.example.gymapprefactor.features.navigation.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducerImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@OptIn(ExperimentalCoroutinesApi::class)
class NavigationViewModelImplTest {

    private val navigationStateFlow = MutableStateFlow<NavigationState>(NavigationState.None)

    private val navigationReducer = mockk<NavigationReducerImpl> {
        coEvery { state } returns navigationStateFlow
        coEvery { update(any()) } just Runs
    }

    @Test
    fun `Given sut and None state, When init, Then default home screen expected`() = runTest {
        // Given
        navigationStateFlow.emit(NavigationState.None)

        // When
        getSut()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.HomeScreen))
        }
    }

    @Test
    fun `Given sut, When page change emitted, Then Screen Called`() = runTest {
        // Given
        navigationStateFlow.emit(NavigationState.None)

        // When
        getSut()
        advanceUntilIdle()

        navigationStateFlow.emit(NavigationState.CurrentPage(NavigationPage.EditRoutineScreen))
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
        }
    }

    @Test
    fun `Given sut, When init, Then onBack callback sent and collection init expected`() = runTest {
        // Given
        val mockNavigationStateToTestCollection = mockk<NavigationState.CurrentPage> {
            coEvery { page } returns NavigationPage.BaseRoutineScreen
        }

        val action = slot<NavigationAction.SetCallbacks>()
        coEvery { navigationReducer.update(capture(action)) } returns Unit

        // When
        getSut()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.HomeScreen))
        }

        val onBackCallback = action.captured.onBack
        navigationStateFlow.emit(mockNavigationStateToTestCollection)
        advanceUntilIdle()

        onBackCallback()
        advanceUntilIdle()

        coVerify(exactly = 2) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.HomeScreen))
        }
    }

    private fun getSut(): NavigationViewModelImpl {
        return NavigationViewModelImpl(
            navigationReducer = navigationReducer,
            TestDispatcherProvider(CoroutinesTestExtension.testDispatcher)
        )
    }
}
