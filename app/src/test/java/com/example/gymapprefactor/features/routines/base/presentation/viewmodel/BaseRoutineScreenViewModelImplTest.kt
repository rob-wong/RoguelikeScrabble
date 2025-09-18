package com.example.gymapprefactor.features.routines.base.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.routines.domain.GetRoutinesUseCase
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducerImpl
import com.example.gymapprefactor.features.routines.base.presentation.mappers.RoutineToButtonStateMapper
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenAction
import com.example.gymapprefactor.features.routines.base.presentation.models.BaseRoutineScreenState
import com.example.gymapprefactor.features.routines.base.presentation.state.BaseRoutineScreenReducerImpl
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventState
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducerImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@OptIn(ExperimentalCoroutinesApi::class)
class BaseRoutineScreenViewModelImplTest {
    private val baseRoutineScreenReducer = mockk<BaseRoutineScreenReducerImpl> {
        every { state.value } returns BaseRoutineScreenState.None
        coEvery { update(any()) } just Runs
    }

    private val editRoutineEventReducer = mockk<RoutineEventReducerImpl> {
        every { state.value } returns EditRoutineEventState.None
    }

    private val navigationStateFlow = MutableStateFlow<NavigationState>(NavigationState.None)

    private val navigationReducer = spyk<NavigationReducerImpl> {
        every { state } returns navigationStateFlow
        coEvery { update(any()) } just Runs
    }

    private val mockRoutine = mockk<Routine>()
    private val getRoutinesUseCase = mockk<GetRoutinesUseCase>()

    private val routineButton = mockk<RectangleButtonState.Content>()
    private val routineToButtonStateMapper = mockk<RoutineToButtonStateMapper> {
        every { map(any()) } returns routineButton
    }

    @Test
    fun `Given sut, When init, Then SetContent expected`() = runTest {
        // Given
        val action = slot<BaseRoutineScreenAction>()
        coEvery { baseRoutineScreenReducer.update(capture(action)) } returns Unit

        // When
        getSut()
        advanceUntilIdle()

        // Then
        assertTrue(action.captured is BaseRoutineScreenAction.SetContent)
        coVerify(exactly = 1) { baseRoutineScreenReducer.update(action.captured) }
    }

    @Test
    fun `Given sut, When init, Then init collection expected`() = runTest {
        // Given
        val action = slot<BaseRoutineScreenAction>()
        coEvery { baseRoutineScreenReducer.update(capture(action)) } returns Unit

        // When
        getSut()
        advanceUntilIdle()

        assertTrue(action.captured is BaseRoutineScreenAction.SetContent)
        coVerify(exactly = 1) { baseRoutineScreenReducer.update(action.captured) }

        // Then
        navigationStateFlow.emit(NavigationState.CurrentPage(NavigationPage.BaseRoutineScreen))
        advanceUntilIdle()

        assertTrue(action.captured is BaseRoutineScreenAction.SetContent)
        coVerify(exactly = 2) { baseRoutineScreenReducer.update(any()) }
    }

    @Test
    fun `Given init, When onBack, Then NavigationReducer call expected`() = runTest {
        // Given
        val action = slot<BaseRoutineScreenAction.SetContent>()
        coEvery { baseRoutineScreenReducer.update(capture(action)) } returns Unit

        getSut()
        advanceUntilIdle()

        // When
        (action.captured.topBarState as TopBarState.Content).onBack()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { navigationReducer.update(NavigationAction.GoBack) }
    }

    @Test
    fun `Given sut initialized, When goToEditRoutine, Then navigation and event call expected`() =
        runTest {
            // Given
            val action = slot<RoutineToButtonStateMapper.Param>()
            coEvery {
                routineToButtonStateMapper.map(capture(action))
            } returns RectangleButtonState.None
            coEvery {
                navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
            } just Runs
            coEvery {
                editRoutineEventReducer.update(EditRoutineEventAction.SelectRoutine(mockRoutine))
            } just Runs

            // When
            getSut()
            advanceUntilIdle()

            // Then
            assertTrue(action.isCaptured)

            action.captured.navigationCallback()
            advanceUntilIdle()
            coVerify(exactly = 1) {
                navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
            }

            action.captured.editRoutineEventCallback(mockRoutine)
            advanceUntilIdle()
            coVerify(exactly = 1) {
                editRoutineEventReducer.update(EditRoutineEventAction.SelectRoutine(mockRoutine))
            }
        }

    @Test
    fun `Given sut initialized, When getCreateRoutineButton, Then navigation, event call expected`() = runTest {
        // Given
        val action = slot<BaseRoutineScreenAction>()
        coEvery { baseRoutineScreenReducer.update(capture(action)) } returns Unit

        coEvery {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
        } just Runs
        coEvery {
            editRoutineEventReducer.update(EditRoutineEventAction.CreateRoutine)
        } just Runs

        // When
        getSut()
        advanceUntilIdle()

        // Then
        assertTrue(action.captured is BaseRoutineScreenAction.SetContent)
        val result = action.captured as BaseRoutineScreenAction.SetContent

        assertTrue(result.createRoutineButton is RectangleButtonState.Content)
        val resultButton = result.createRoutineButton as RectangleButtonState.Content

        resultButton.onClick()
        advanceUntilIdle()
        coVerify(exactly = 1) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.EditRoutineScreen))
            editRoutineEventReducer.update(EditRoutineEventAction.CreateRoutine)
        }
    }

    private fun getSut(): BaseRoutineScreenViewModelImpl {
        coEvery { getRoutinesUseCase() } returns listOf(mockRoutine)

        return BaseRoutineScreenViewModelImpl(
            baseRoutineScreenReducer = baseRoutineScreenReducer,
            routineEventReducer = editRoutineEventReducer,
            getRoutinesUseCase = getRoutinesUseCase,
            routineToButtonStateMapper = routineToButtonStateMapper,
            dispatcherProvider = TestDispatcherProvider(CoroutinesTestExtension.testDispatcher),
            navigationReducer = navigationReducer
        )
    }
}
