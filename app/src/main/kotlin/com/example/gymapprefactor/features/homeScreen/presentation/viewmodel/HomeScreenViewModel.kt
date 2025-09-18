package com.example.gymapprefactor.features.homeScreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.homeScreen.presentation.models.HomeScreenState
import kotlinx.coroutines.flow.Flow

abstract class HomeScreenViewModel : ViewModel() {

    abstract val state: Flow<HomeScreenState>
}
