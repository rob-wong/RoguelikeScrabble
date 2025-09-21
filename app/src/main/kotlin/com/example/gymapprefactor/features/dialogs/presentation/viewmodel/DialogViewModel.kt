package com.example.gymapprefactor.features.dialogs.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gymapprefactor.features.dialogs.presentation.models.DialogState
import kotlinx.coroutines.flow.Flow

abstract class DialogViewModel : ViewModel() {
	abstract val state: Flow<DialogState>
}
