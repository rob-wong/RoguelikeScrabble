package com.example.gymapprefactor.features.routines.edit.presentation.viewmodel

import com.example.gymapprefactor.app.util.dispatcher.TestDispatcherProvider
import com.example.gymapprefactor.business.models.DefaultRoutine
import com.example.gymapprefactor.business.models.Routine
import com.example.gymapprefactor.business.models.exercise.Template
import com.example.gymapprefactor.business.routines.domain.CreateRoutineUseCase
import com.example.gymapprefactor.business.routines.domain.SaveRoutineUseCase
import com.example.gymapprefactor.common.components.presentation.RectangleButtonState
import com.example.gymapprefactor.common.components.presentation.TextInputFieldState
import com.example.gymapprefactor.common.components.presentation.TopBarState
import com.example.gymapprefactor.features.exercises.events.ExerciseEventAction
import com.example.gymapprefactor.features.exercises.events.ExerciseEventReducer
import com.example.gymapprefactor.features.exercises.events.ExerciseEventState
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.state.NavigationReducer
import com.example.gymapprefactor.features.routines.base.presentation.mappers.TemplateToButtonStateMapper
import com.example.gymapprefactor.features.routines.base.presentation.mappers.TemplateToButtonStateMapperImpl
import com.example.gymapprefactor.features.routines.edit.presentation.models.EditRoutineAction
import com.example.gymapprefactor.features.routines.edit.presentation.models.events.EditRoutineEventState
import com.example.gymapprefactor.features.routines.edit.presentation.state.EditRoutineReducerImpl
import com.example.gymapprefactor.features.routines.edit.presentation.state.events.RoutineEventReducerImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
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
class EditRoutineViewModelImplTest {

    private val action = slot<EditRoutineAction.SetContent>()
    private val editRoutineReducer = mockk<EditRoutineReducerImpl>(relaxed = true)
    private val eventStateFlow = MutableStateFlow<EditRoutineEventState>(EditRoutineEventState.None)

    private val editRoutineEventReducer = mockk<RoutineEventReducerImpl> {
        every { state } returns eventStateFlow
    }

    private val mapperParam = slot<TemplateToButtonStateMapper.Param>()
    private val templateToButtonStateMapper = mockk<TemplateToButtonStateMapperImpl>()
    private val navigationReducer = mockk<NavigationReducer>(relaxed = true)
    private val exerciseEventReducer = mockk<ExerciseEventReducer>(relaxed = true)

    private val mockTemplate = mockk<Template>()
    private val routineName = "routine name"
    private val routineId = "routine-id"
    private val mockRoutine = mockk<Routine> {
        every { name } returns routineName
        every { id } returns routineId
        every { templates } returns listOf(mockTemplate)
    }
    private val mockEventSelectRoutineState: EditRoutineEventState.SelectRoutine = mockk {
        every { routine } returns mockRoutine
    }

    private val createRoutineUseCase: CreateRoutineUseCase = mockk(relaxed = true)
    private val saveRoutineUseCase: SaveRoutineUseCase = mockk(relaxed = true)

    @Test
    fun `Given None state, When sut init, Then Unit expected`() = runTest {
        // Given
        val mockNoneState = mockk<EditRoutineEventState.None>()
        eventStateFlow.emit(mockNoneState)

        // When
        getSut()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { editRoutineReducer.update(any()) }
    }

    @Test
    fun `Given SelectRoutineState, When init, Then SetContent and callback mapping expected`() = runTest {
        // Given
        coEvery { editRoutineReducer.update(capture(action)) } just Runs

        coEvery {
            templateToButtonStateMapper.map(capture(mapperParam))
        } returns RectangleButtonState.None

        eventStateFlow.emit(mockEventSelectRoutineState)

        // When
        getSut()
        advanceUntilIdle()

        // Then
        assertEquals("Edit Routine", (action.captured.topBarState as TopBarState.Content).title)
        assertEquals(routineName, (action.captured.nameInputField as TextInputFieldState.Content).text)
        assertEquals(mockTemplate, mapperParam.captured.template)

        action.captured.onClickAddExercise()
        advanceUntilIdle()
        coVerify(exactly = 1) {
            navigationReducer.update(NavigationAction.GoTo(NavigationPage.PickExerciseScreen))
        }
        coVerify(exactly = 1) {
            exerciseEventReducer.update(
                ExerciseEventAction.PickExercise(
                    ExerciseEventState.PickEndPage.Routines(mockRoutine)
                )
            )
        }

        action.captured.onSave() // onSave, onBack
        advanceUntilIdle()
        coVerify(exactly = 1) { navigationReducer.update(NavigationAction.GoBack) }
    }

    @Test
    fun `Given init, When onClickRemoveExercise and onNameValueChanged, Then UseCase call expected`() = runTest {
        // Given
        coEvery { editRoutineReducer.update(capture(action)) } just Runs
        coEvery {
            templateToButtonStateMapper.map(capture(mapperParam))
        } returns RectangleButtonState.None

        val mockTemplateToRemove = mockk<Template> {
            every { id } returns "template-id"
        }
        val mockTemplateToStay = mockk<Template> {
            every { id } returns "staying-template-id"
        }
        val unMockedRoutine = DefaultRoutine(
            templates = listOf(mockTemplateToRemove, mockTemplateToStay),
            name = "routine-name",
            id = "routine-id"
        )
        every { mockEventSelectRoutineState.routine } returns unMockedRoutine

        eventStateFlow.emit(mockEventSelectRoutineState)
        getSut()
        advanceUntilIdle()

        // When
        assertEquals(2, action.captured.exerciseSetupList.size)
        mapperParam.captured.removeExerciseCallback("template-id")
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { saveRoutineUseCase(any()) }
        assertEquals(1, action.captured.exerciseSetupList.size)

        (action.captured.nameInputField as TextInputFieldState.Content).onValueChanged("mock")
        advanceUntilIdle()
        coVerify(exactly = 2) { saveRoutineUseCase(any()) }
    }

    @Test
    fun `Given RoutineUpdated state, When sut init, Then setContent expected`() = runTest {
        // Given
        val mockRoutineUpdatedState = mockk<EditRoutineEventState.RoutineUpdated> {
            every { routine } returns mockRoutine
        }
        coEvery { editRoutineReducer.update(capture(action)) } just Runs
        coEvery {
            templateToButtonStateMapper.map(capture(mapperParam))
        } returns RectangleButtonState.None

        eventStateFlow.emit(mockEventSelectRoutineState)

        // When
        getSut()
        advanceUntilIdle()
        coVerify(exactly = 1) { editRoutineReducer.update(any()) }

        eventStateFlow.emit(mockRoutineUpdatedState)
        advanceUntilIdle()

        assertEquals(routineName, (action.captured.nameInputField as TextInputFieldState.Content).text)
        coVerify(exactly = 2) { editRoutineReducer.update(any()) }
    }

    @Test
    fun `Given CreateRoutine state, When sut init, Then routine created expected`() = runTest {
        // Given
        val mockCreateRoutineState = mockk<EditRoutineEventState.CreateRoutine>()
        coEvery {
            templateToButtonStateMapper.map(capture(mapperParam))
        } returns RectangleButtonState.None
        coEvery { editRoutineReducer.update(capture(action)) } just Runs
        coEvery { createRoutineUseCase() } returns mockRoutine
        eventStateFlow.emit(mockCreateRoutineState)

        // When
        getSut()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { createRoutineUseCase() }
    }

    private fun getSut(): EditRoutineViewModel {
        return EditRoutineViewModelImpl(
            editRoutineReducer = editRoutineReducer,
            routineEventReducer = editRoutineEventReducer,
            createRoutineUseCase = createRoutineUseCase,
            saveRoutineUseCase = saveRoutineUseCase,
            exerciseEventReducer = exerciseEventReducer,
            navigationReducer = navigationReducer,
            templateToButtonStateMapper = templateToButtonStateMapper,
            dispatcherProvider = TestDispatcherProvider(CoroutinesTestExtension.testDispatcher)
        )
    }
}
