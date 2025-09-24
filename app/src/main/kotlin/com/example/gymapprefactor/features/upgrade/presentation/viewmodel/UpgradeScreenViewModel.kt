package com.example.gymapprefactor.features.upgrade.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.upgrade.presentation.models.UpgradeScreenState
import kotlinx.coroutines.flow.Flow

sealed class UpgradeScreenViewModel : ViewModel() {
	abstract val state: Flow<UpgradeScreenState>
}
