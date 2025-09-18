package com.example.gymapprefactor.features.navigation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.navigation.presentation.models.NavigationState
import kotlinx.coroutines.flow.Flow

abstract class NavigationViewModel : ViewModel() {

    abstract val state: Flow<NavigationState>
}
