package com.example.gymapprefactor.features.homeScreen.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.common.components.presentation.adapters.RectangleButtonMapper
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenAction
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState
import com.example.gymapprefactor.features.homeScreen.presentation.state.HomeScreenReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducerImpl
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelImplTest {

    private val mockState = mockk<MutableStateFlow<HomeScreenState>> {
        coEvery { value } returns HomeScreenState.None
    }

    private val homeScreenReducer = mockk<HomeScreenReducer> {
        coEvery { state } returns mockState
    }

    private val rectangleButtonMapper = mockk<RectangleButtonMapper> {
        every { map(any()) } returns mockk<RectangleButtonState.Content>(relaxed = true)
    }

    private val navigationReducer = mockk<NavigationReducerImpl> {
        every { state.value } returns NavigationState.None
    }

    private val testScope = TestScope(CoroutinesTestExtension.testDispatcher)

    @Test
    fun `Given sut, When init, Then SetContent Expected`() = runTest {
        // Given
        val action = slot<HomeScreenAction>()
        coEvery { homeScreenReducer.update(capture(action)) } returns Unit

        // When
        getSut()
        advanceUntilIdle()

        // Then
        assertTrue(action.isCaptured)
        assertTrue(action.captured is HomeScreenAction.SetContent)
        coVerify(exactly = 1) { homeScreenReducer.update(action.captured) }
    }

    @Test
    fun `Given sut, When onRoutines invoked, Then navigation call expected`() = runTest {
        // Given
        coEvery {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.BaseRoutineScreen))
        } just Runs
        val action = initViewModel()

        // When
        (action.onRoutines as RectangleButtonState.Content).onClick()
        advanceUntilIdle()

        // Then
    }

    @Test
    fun `Given sut, When onExercises invoked, Then        expected`() = runTest {
        // Given
        val action = initViewModel()

        // When
        (action.onExercises as RectangleButtonState.Content).onClick()
        advanceUntilIdle()

        // Then
    }

    @Test
    fun `Given sut, When onViewStats invoked, Then        expected`() = runTest {
        // Given
        val action = initViewModel()

        // When
        (action.onViewStats as RectangleButtonState.Content).onClick()
        advanceUntilIdle()

        // Then
    }

    @Test
    fun `Given sut, When onBack, Then NavigationReducer call expected`() = runTest {
        // Given
        coEvery { navigationReducer.update(NavigationAction.GoBack) } just Runs
        val action = initViewModel()

        // When
        (action.topBarState as TopBarState.Content).onBack()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { navigationReducer.update(NavigationAction.GoBack) }
    }

    private fun initViewModel(): HomeScreenAction.SetContent {
        // Given
        val action = slot<HomeScreenAction.SetContent>()
        coEvery { homeScreenReducer.update(capture(action)) } returns Unit

        // When
        getSut()
        testScope.advanceUntilIdle()

        // Then
        assertTrue(action.isCaptured)
        return action.captured
    }

    private fun getSut(): HomeScreenViewModelImpl {
        return HomeScreenViewModelImpl(
            homeScreenReducer,
            rectangleButtonMapper,
            navigationReducer,
            TestDispatcherProvider(CoroutinesTestExtension.testDispatcher)
        )
    }
}
