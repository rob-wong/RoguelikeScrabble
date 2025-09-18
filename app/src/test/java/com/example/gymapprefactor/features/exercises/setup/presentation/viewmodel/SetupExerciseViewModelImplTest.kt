package com.example.gymapprefactor.features.exercises.setup.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Exercise
import com.example.gymapprefactor.business.models.exercise.Template
import com.example.gymapprefactor.business.routines.domain.SaveRoutineUseCase
import com.example.gymapprefactor.business.templates.domain.CreateTemplateUseCase
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventReducerImpl
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import com.example.gymapprefactor.features.exercises.setup.presentation.models.SetupExerciseAction
import com.example.gymapprefactor.features.exercises.setup.presentation.state.SetupExerciseReducer
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventAction
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducer
import io.mockk.coEvery
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
class SetupExerciseViewModelImplTest {

    private val mockExercise = mockk<Exercise>()
    private val setupExerciseState = mockk<ExerciseEventState.SetupExercise> {
        every { exercise } returns mockExercise
    }
    private val exerciseEventStateFlow = MutableStateFlow<ExerciseEventState>(ExerciseEventState.None)
    private val mockExerciseEventReducer = mockk<ExerciseEventReducerImpl> {
        every { state } returns exerciseEventStateFlow
    }

    private val mockSetupExerciseReducer = mockk<SetupExerciseReducer>(relaxed = true)
    private val mockRoutineEventReducer = mockk<RoutineEventReducer>(relaxed = true)

    private val mockCreatedTemplate = mockk<Template>()
    private val mockCreateTemplateUseCase = mockk<CreateTemplateUseCase>(relaxed = true)

    private val mockUpdatedRoutine = mockk<Routine>()
    private val mockSaveRoutineUseCase = mockk<SaveRoutineUseCase>()
    private val mockNavigationReducer = mockk<NavigationReducer>(relaxed = true)

    @Test
    fun `Given ExerciseEventState not SetupExercise, When init, Then unit expected`() = runTest {
        // GIVEN
        val noneEventState = ExerciseEventState.None
        exerciseEventStateFlow.emit(noneEventState)

        // WHEN
        getSut()
        advanceUntilIdle()

        // THEN
        coVerify(exactly = 0) { mockSetupExerciseReducer.update(any()) }
    }

    @Test
    fun `Given ExerciseEventState is SetupExercise, When init, Then SetContent expected`() = runTest {
        // GIVEN
        val mockEndPage = mockk<ExerciseEventState.SetupEndPage.Begin>()
        every { setupExerciseState.setupEndPage } returns mockEndPage
        exerciseEventStateFlow.emit(setupExerciseState)

        // WHEN
        getSut()
        advanceUntilIdle()

        // THEN
        val slot = slot<SetupExerciseAction.SetContent>()
        coVerify { mockSetupExerciseReducer.update(capture(slot)) }
        val captured = slot.captured

        assertEquals((captured.topBarState as TopBarState.Content).title, "Setup Exercise")
    }

    @Test
    fun `Given ExerciseEventState is SetupExercise and routines endPage, When onSave, Then correct action expected`() =
        runTest {
            // GIVEN
            val mockRoutine = DefaultRoutine(
                id = "routine-id",
                name = "routine-name",
                templates = listOf()
            )
            val routinesEndPage = mockk<ExerciseEventState.SetupEndPage.Routines> {
                every { routine } returns mockRoutine
            }
            every { setupExerciseState.setupEndPage } returns routinesEndPage
            coEvery { mockSaveRoutineUseCase(any()) } returns mockUpdatedRoutine
            coEvery { mockCreateTemplateUseCase(any(), any()) } returns mockCreatedTemplate

            exerciseEventStateFlow.emit(setupExerciseState)

            // WHEN
            getSut()
            advanceUntilIdle()

            // THEN
            val slot = slot<SetupExerciseAction.SetContent>()
            coVerify { mockSetupExerciseReducer.update(capture(slot)) }
            val captured = slot.captured

            (captured.onSave as RectangleButtonState.Content).onClick()
            advanceUntilIdle()

            coVerify {
                mockRoutineEventReducer.update(
                    EditRoutineEventAction.RoutineUpdated(mockUpdatedRoutine)
                )
            }
        }

    @Test
    fun `Given ExerciseEventState is SetupExercise and begin endPage, when onSave, Then none state expected`() =
        runTest {
            // GIVEN
            val beginEndPage = mockk<ExerciseEventState.SetupEndPage.Begin>()
            every { setupExerciseState.setupEndPage } returns beginEndPage
            coEvery { mockCreateTemplateUseCase(any(), any()) } returns mockCreatedTemplate

            exerciseEventStateFlow.emit(setupExerciseState)

            // WHEN
            getSut()
            advanceUntilIdle()

            // THEN
            val slot = slot<SetupExerciseAction.SetContent>()
            coVerify { mockSetupExerciseReducer.update(capture(slot)) }
            val captured = slot.captured

            assertEquals(captured.onSave, RectangleButtonState.None)
        }

    @Test
    fun `Given init, When onBack, Then NavigationReducer call expected`() = runTest {
        // GIVEN
        val mockEndPage = mockk<ExerciseEventState.SetupEndPage.Begin>()
        every { setupExerciseState.setupEndPage } returns mockEndPage
        exerciseEventStateFlow.emit(setupExerciseState)

        getSut()
        advanceUntilIdle()

        val slot = slot<SetupExerciseAction.SetContent>()
        coVerify { mockSetupExerciseReducer.update(capture(slot)) }
        val captured = slot.captured

        // WHEN
        (captured.topBarState as TopBarState.Content).onBack()
        advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) { mockNavigationReducer.update(NavigationAction.GoBack) }
    }

    private fun getSut(): SetupExerciseViewModelImpl {
        return SetupExerciseViewModelImpl(
            setupExerciseReducer = mockSetupExerciseReducer,
            exerciseEventReducer = mockExerciseEventReducer,
            routineEventReducer = mockRoutineEventReducer,
            createTemplateUseCase = mockCreateTemplateUseCase,
            saveRoutineUseCase = mockSaveRoutineUseCase,
            navigationReducer = mockNavigationReducer,
            dispatcherProvider = TestDispatcherProvider(CoroutinesTestExtension.testDispatcher)
        )
    }
}
