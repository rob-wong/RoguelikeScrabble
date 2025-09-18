package com.example.gymapprefactor.features.navigation.presentation.state

import com.example.gymapprefactor.features.navigation.presentation.models.NavigationAction
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationPage
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import testextension.CoroutinesTestExtension

@ExtendWith(CoroutinesTestExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class NavigationReducerImplTest {
    private val sut = NavigationReducerImpl()

    private val mockHomeScreenPage = mockk<NavigationPage.HomeScreen>()

    @Test
    fun `Given None action, When update called, Then unchanged state expected`() = runTest {
        // Given
        val action = NavigationAction.None
        val originalState = sut.state.value

        // When
        sut.update(action)

        // Then
        assertEquals(originalState, sut.state.value)
    }

    @Test
    fun `Given GoTo action, When update called, Then CurrentPage state expected`() = runTest {
        // Given
        val action = NavigationAction.GoTo(mockHomeScreenPage)

        // When
        sut.update(action)

        // Then
        val expectedState = NavigationState.CurrentPage(mockHomeScreenPage)
        assertEquals(expectedState, sut.state.value)
    }

    @Test
    fun `Given SetCallbacks action, When update, Then callbacks expected`() = runTest {
        // GIVEN
        val mockOnBack: () -> Unit = mockk(relaxed = true)
        val action = NavigationAction.SetCallbacks(mockOnBack)

        // WHEN
        sut.update(action)

        // THEN
        val testingAction = NavigationAction.GoBack
        sut.update(testingAction)
        verify(exactly = 1) { mockOnBack() }
    }
}
