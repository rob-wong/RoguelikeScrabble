package com.example.gymapprefactor.features.exercises.pick.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.business.exercises.domain.GetExercisesUseCase
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventAction
import com.example.gymapprefactor.features.exercises.events.ExerciseEventReducerImpl
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import com.example.gymapprefactor.features.exercises.pick.presentation.mappers.PickTemplateToButtonListMapper
import com.example.gymapprefactor.features.exercises.pick.presentation.mappers.PickTemplateToButtonListMapperImpl
import com.example.gymapprefactor.features.exercises.pick.presentation.models.PickExerciseAction
import com.example.gymapprefactor.features.exercises.pick.presentation.state.PickExerciseReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducerImpl
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@OptIn(ExperimentalCoroutinesApi::class)
class PickExerciseViewModelImplTest {

    private val pickExerciseState = mockk<ExerciseEventState.PickExercise>(relaxed = true)
    private val exerciseEventStateFlow = MutableStateFlow<ExerciseEventState>(ExerciseEventState.None)
    private val mockExerciseEventReducer = mockk<ExerciseEventReducerImpl>(relaxed = true) {
        every { state } returns exerciseEventStateFlow
    }

    private val mockButtonList = listOf<RectangleButtonState>()
    private val mockPickTemplateToButtonListMapper =
        mockk<PickTemplateToButtonListMapperImpl>(relaxed = true) {
            every { map(any()) } returns mockButtonList
        }

    private val mockGetExercisesUseCase = mockk<GetExercisesUseCase>(relaxed = true)
    private val mockPickExerciseReducer = mockk<PickExerciseReducer>(relaxed = true)
    private val mockNavigationReducer = mockk<NavigationReducerImpl>(relaxed = true)

    @Test
    fun `Given ExerciseEventState not PickExercise, When init, Then unit expected`() = runTest {
        // GIVEN
        val noneEventState = ExerciseEventState.None
        exerciseEventStateFlow.emit(noneEventState)

        // WHEN
        getSut()
        advanceUntilIdle()

        // THEN
        coVerify(exactly = 0) { mockPickExerciseReducer.update(any()) }
    }

    @Test
    fun `Given ExerciseEventState is PickExercise, When init, Then SetContent expected`() =
        runTest {
            // GIVEN
            exerciseEventStateFlow.emit(pickExerciseState)

            // WHEN
            getSut()
            advanceUntilIdle()

            // THEN
            val slot = slot<PickExerciseAction.SetContent>()
            coVerify { mockPickExerciseReducer.update(capture(slot)) }
            val captured: PickExerciseAction.SetContent = slot.captured

            assertEquals((captured.exerciseList), mockButtonList)
            assertEquals((captured.topBarState as TopBarState.Content).title, "Pick Exercise")
        }

    @Test
    fun `Given PickExercise event state and routines endPage, When onClick, Then correct action expected`() =
        runTest {
            // GIVEN
            val mockRoutine = mockk<Routine>()
            val mockRoutinesEndPage = mockk<ExerciseEventState.PickEndPage.Routines> {
                every { routine } returns mockRoutine
            }
            every { pickExerciseState.endPage } returns mockRoutinesEndPage
            exerciseEventStateFlow.emit(pickExerciseState)

            // WHEN
            getSut()
            advanceUntilIdle()

            // THEN
            val slot = slot<PickTemplateToButtonListMapper.Param>()
            coVerify { mockPickTemplateToButtonListMapper.map(capture(slot)) }
            val captured: PickTemplateToButtonListMapper.Param = slot.captured
            val exerciseForTestingOnClick = mockk<Exercise>()

            captured.onClick(exerciseForTestingOnClick)
            advanceUntilIdle()

            coVerify {
                mockExerciseEventReducer.update(
                    ExerciseEventAction.SetupExercise(
                        exercise = exerciseForTestingOnClick,
                        setupEndPage = ExerciseEventState.SetupEndPage.Routines(mockRoutine)
                    )
                )
            }
        }

    @Test
    fun `Given PickExercise event state and stats endPage, When onClick, Then correct action expected`() =
        runTest {
            // GIVEN
            val mockStatsEndPage = mockk<ExerciseEventState.PickEndPage.Stats>()
            every { pickExerciseState.endPage } returns mockStatsEndPage
            exerciseEventStateFlow.emit(pickExerciseState)

            // WHEN
            getSut()
            advanceUntilIdle()

            // THEN
            val slot = slot<PickTemplateToButtonListMapper.Param>()
            coVerify { mockPickTemplateToButtonListMapper.map(capture(slot)) }
            val captured: PickTemplateToButtonListMapper.Param = slot.captured
            val exerciseForTestingOnClick = mockk<Exercise>() {
                every { name } returns ""
            }

            captured.onClick(exerciseForTestingOnClick)
            advanceUntilIdle()

            coVerify { mockExerciseEventReducer.update(ExerciseEventAction.None) }
        }

    @Test
    fun `Given init, When onBack, Then NavigationReducer call expected`() =
        runTest {
            // GIVEN
            exerciseEventStateFlow.emit(pickExerciseState)
            getSut()
            advanceUntilIdle()

            val slot = slot<PickExerciseAction.SetContent>()
            coVerify { mockPickExerciseReducer.update(capture(slot)) }
            val captured: PickExerciseAction.SetContent = slot.captured

            // WHEN
            (captured.topBarState as TopBarState.Content).onBack()
            advanceUntilIdle()

            // THEN
            coVerify(exactly = 1) { mockNavigationReducer.update(NavigationAction.GoBack) }
        }

    private fun getSut(): PickExerciseViewModelImpl {
        return PickExerciseViewModelImpl(
            exerciseEventReducer = mockExerciseEventReducer,
            pickTemplateToButtonListMapper = mockPickTemplateToButtonListMapper,
            getExercisesUseCase = mockGetExercisesUseCase,
            pickExerciseReducer = mockPickExerciseReducer,
            navigationReducer = mockNavigationReducer,
            dispatcherProvider = TestDispatcherProvider(CoroutinesTestExtension.testDispatcher)
        )
    }
}
