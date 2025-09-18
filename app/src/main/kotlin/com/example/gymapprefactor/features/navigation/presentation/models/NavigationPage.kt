package com.example.gymapprefactor.features.navigation.presentation.models

sealed class NavigationPage {
    data object HomeScreen : NavigationPage()
    data object BaseRoutineScreen : NavigationPage()
    data object EditRoutineScreen : NavigationPage()
    data object PickExerciseScreen : NavigationPage()
    data object SetupExerciseScreen : NavigationPage()
}
