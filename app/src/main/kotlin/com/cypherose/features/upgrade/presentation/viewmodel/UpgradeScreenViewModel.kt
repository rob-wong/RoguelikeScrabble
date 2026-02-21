package com.cypherose.features.upgrade.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cypherose.features.upgrade.presentation.models.UpgradeScreenState
import kotlinx.coroutines.flow.Flow

sealed class UpgradeScreenViewModel : ViewModel() {
	abstract val state: Flow<UpgradeScreenState>
}
